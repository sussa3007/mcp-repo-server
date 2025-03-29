package com.miraclestudio.mcpreposerver.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 로그 파일을 주기적으로 정리하는 스케줄러
 */
@Slf4j
@Component
public class LogCleanupScheduler {

    @Value("${log.path:./logs}")
    private String logPath;

    @Value("${log.error.retention-days:7}")
    private int errorLogRetentionDays;

    @Value("${log.retention-days:30}")
    private int logRetentionDays;

    /**
     * 매일 새벽 3시에 오래된 로그 파일 정리
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupLogs() {
        log.info("로그 파일 정리 작업 시작");

        // 에러 로그 정리
        cleanErrorLogs();

        // 일반 로그 정리
        cleanGeneralLogs();

        log.info("로그 파일 정리 작업 완료");
    }

    /**
     * 에러 로그 정리
     */
    private void cleanErrorLogs() {
        String errorLogDir = logPath + "/errors";
        try {
            Path errorPath = Paths.get(errorLogDir);

            // 디렉토리가 존재하는지 확인
            if (!Files.exists(errorPath)) {
                log.info("에러 로그 디렉토리가 존재하지 않습니다: {}", errorLogDir);
                return;
            }

            // 로그 파일 목록 가져오기
            List<Path> errorLogFiles = Files.list(errorPath)
                    .filter(path -> {
                        String fileName = path.getFileName().toString();
                        return fileName.startsWith("error.") && fileName.endsWith(".log");
                    })
                    .collect(Collectors.toList());

            // 파일 정렬 (오래된 순)
            errorLogFiles.sort(Comparator.comparing(path -> {
                try {
                    return Files.getLastModifiedTime(path).toMillis();
                } catch (IOException e) {
                    return 0L;
                }
            }));

            // 보관 기간이 지난 파일 삭제
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(errorLogRetentionDays);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Path file : errorLogFiles) {
                String fileName = file.getFileName().toString();
                // error.2023-01-01.log 형식에서 날짜 추출
                if (fileName.length() > 11) {
                    String dateStr = fileName.substring(6, 16);
                    try {
                        LocalDateTime fileDate = LocalDateTime.parse(dateStr + " 00:00:00",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        if (fileDate.isBefore(cutoffDate)) {
                            Files.delete(file);
                            log.info("오래된 에러 로그 파일 삭제: {}", fileName);
                        }
                    } catch (Exception e) {
                        log.warn("로그 파일 날짜 파싱 오류: {}", fileName, e);
                    }
                }
            }

            log.info("에러 로그 정리 완료: {} 파일 처리됨", errorLogFiles.size());
        } catch (Exception e) {
            log.error("에러 로그 정리 중 오류 발생", e);
        }
    }

    /**
     * 일반 로그 정리
     */
    private void cleanGeneralLogs() {
        try {
            Path logDir = Paths.get(logPath);

            // 디렉토리가 존재하는지 확인
            if (!Files.exists(logDir)) {
                log.info("로그 디렉토리가 존재하지 않습니다: {}", logPath);
                return;
            }

            // 로그 파일 목록 가져오기
            List<Path> logFiles = Files.list(logDir)
                    .filter(path -> {
                        String fileName = path.getFileName().toString();
                        return fileName.startsWith("mcpreposerver.") && fileName.endsWith(".log");
                    })
                    .collect(Collectors.toList());

            // 파일 정렬 (오래된 순)
            logFiles.sort(Comparator.comparing(path -> {
                try {
                    return Files.getLastModifiedTime(path).toMillis();
                } catch (IOException e) {
                    return 0L;
                }
            }));

            // 보관 기간이 지난 파일 삭제
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(logRetentionDays);

            for (Path file : logFiles) {
                String fileName = file.getFileName().toString();
                // mcpreposerver.2023-01-01.log 형식에서 날짜 추출
                if (fileName.length() > 18) {
                    String dateStr = fileName.substring(14, 24);
                    try {
                        LocalDateTime fileDate = LocalDateTime.parse(dateStr + " 00:00:00",
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        if (fileDate.isBefore(cutoffDate)) {
                            Files.delete(file);
                            log.info("오래된 일반 로그 파일 삭제: {}", fileName);
                        }
                    } catch (Exception e) {
                        log.warn("로그 파일 날짜 파싱 오류: {}", fileName, e);
                    }
                }
            }

            log.info("일반 로그 정리 완료: {} 파일 처리됨", logFiles.size());
        } catch (Exception e) {
            log.error("일반 로그 정리 중 오류 발생", e);
        }
    }

    /**
     * 현재 로그 상태 확인 (수동 실행용)
     */
    public void checkLogStatus() {
        File logDir = new File(logPath);
        File errorLogDir = new File(logPath + "/errors");

        log.info("=== 로그 상태 확인 ===");
        log.info("로그 기본 경로: {}", logDir.getAbsolutePath());
        log.info("에러 로그 경로: {}", errorLogDir.getAbsolutePath());

        if (logDir.exists()) {
            long totalSize = calculateDirectorySize(logDir);
            log.info("전체 로그 크기: {}MB", totalSize / (1024 * 1024));

            if (errorLogDir.exists()) {
                long errorSize = calculateDirectorySize(errorLogDir);
                log.info("에러 로그 크기: {}MB", errorSize / (1024 * 1024));
            } else {
                log.info("에러 로그 디렉토리가 존재하지 않습니다.");
            }
        } else {
            log.info("로그 디렉토리가 존재하지 않습니다.");
        }
    }

    /**
     * 디렉토리 크기 계산
     */
    private long calculateDirectorySize(File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }

        long size = 0;
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else if (file.isDirectory()) {
                    size += calculateDirectorySize(file);
                }
            }
        }

        return size;
    }
}