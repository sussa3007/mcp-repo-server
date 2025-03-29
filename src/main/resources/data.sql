-- 사용자 데이터
INSERT IGNORE INTO users (email, name, image_url, role, social_type, social_id, created_at, updated_at) VALUES
('admin@example.com', 'Admin User', 'https://i.pravatar.cc/150?u=admin', 'ADMIN', 'GITHUB', 'github_admin_12345', NOW(), NOW()),
('user1@example.com', 'User One', 'https://i.pravatar.cc/150?u=user1', 'USER', 'GOOGLE', 'google_user1_12345', NOW(), NOW()),
('user2@example.com', 'User Two', 'https://i.pravatar.cc/150?u=user2', 'USER', 'GITHUB', 'github_user2_23456', NOW(), NOW()),
('user3@example.com', 'User Three', 'https://i.pravatar.cc/150?u=user3', 'USER', 'GOOGLE', 'google_user3_23456', NOW(), NOW()),
('user4@example.com', 'User Four', 'https://i.pravatar.cc/150?u=user4', 'USER', 'GITHUB', 'github_user4_34567', NOW(), NOW()),
('user5@example.com', 'User Five', 'https://i.pravatar.cc/150?u=user5', 'USER', 'GOOGLE', 'google_user5_34567', NOW(), NOW());

-- 태그 데이터
INSERT IGNORE INTO tags (name, created_at, updated_at) VALUES
('mcp', NOW(), NOW()),
('server', NOW(), NOW()),
('client', NOW(), NOW()),
('javascript', NOW(), NOW()),
('typescript', NOW(), NOW()),
('java', NOW(), NOW()),
('go', NOW(), NOW()),
('python', NOW(), NOW()),
('rest-api', NOW(), NOW()),
('websocket', NOW(), NOW()),
('real-time', NOW(), NOW()),
('high-performance', NOW(), NOW()),
('authentication', NOW(), NOW()),
('authorization', NOW(), NOW()),
('cross-platform', NOW(), NOW()),
('mobile', NOW(), NOW()),
('desktop', NOW(), NOW()),
('web', NOW(), NOW()),
('dockerized', NOW(), NOW()),
('kubernetes', NOW(), NOW()),
('open-source', NOW(), NOW()),
('lightweight', NOW(), NOW()),
('scalable', NOW(), NOW()),
('secure', NOW(), NOW()),
('cloud-native', NOW(), NOW());

-- 서버 기여자 데이터
INSERT INTO server_contributors (contributor_id, name, username, github_url, avatar_url, created_at, updated_at) VALUES
(1, 'John Developer', 'johndeveloper', 'https://github.com/johndeveloper', 'https://i.pravatar.cc/150?u=johndeveloper', NOW(), NOW()),
(2, 'Sara Coder', 'saracoder', 'https://github.com/saracoder', 'https://i.pravatar.cc/150?u=saracoder', NOW(), NOW()),
(3, 'Mike Engineer', 'mikeengineer', 'https://github.com/mikeengineer', 'https://i.pravatar.cc/150?u=mikeengineer', NOW(), NOW()),
(4, 'Emma Programmer', 'emmaprogrammer', 'https://github.com/emmaprogrammer', 'https://i.pravatar.cc/150?u=emmaprogrammer', NOW(), NOW()),
(5, 'Tom Architect', 'tomarchitect', 'https://github.com/tomarchitect', 'https://i.pravatar.cc/150?u=tomarchitect', NOW(), NOW());

-- 클라이언트 기여자 데이터
INSERT INTO client_contributors (contributor_id, name, username, github_url, avatar_url, created_at, updated_at) VALUES
(1, 'Alice Hacker', 'alicehacker', 'https://github.com/alicehacker', 'https://i.pravatar.cc/150?u=alicehacker', NOW(), NOW()),
(2, 'Bob Builder', 'bobbuilder', 'https://github.com/bobbuilder', 'https://i.pravatar.cc/150?u=bobbuilder', NOW(), NOW()),
(3, 'Carol DevOps', 'caroldevops', 'https://github.com/caroldevops', 'https://i.pravatar.cc/150?u=caroldevops', NOW(), NOW()),
(4, 'Dave Security', 'davesecurity', 'https://github.com/davesecurity', 'https://i.pravatar.cc/150?u=davesecurity', NOW(), NOW()),
(5, 'Erin Frontend', 'erinfrontend', 'https://github.com/erinfrontend', 'https://i.pravatar.cc/150?u=erinfrontend', NOW(), NOW());

-- 서버 레포지토리 데이터
INSERT INTO server_repositories (name, description, owner, repo, github_url, demo_url, is_official, language, stars, forks, license, created_at, updated_at) VALUES
('MCP-Server', 'Official Model Context Protocol server implementation with high-performance features', 'sussa3007', 'mysql-mcp', 'https://github.com/sussa3007/my3sql-mcp', 'https://demo.mcpserver.org', true, 'Go', 1200, 350, 'MIT', NOW(), NOW()),
('Node-MCP-Server', 'Node.js implementation of the MCP server with WebSocket support', 'sussa3007', 'mysql-mcp', 'https://github.com/sussa3007/mysql15-mcp', 'https://node-mcp.example.com', false, 'JavaScript', 850, 210, 'Apache-2.0', NOW(), NOW()),
('FastMCP', 'High-performance MCP server built with Rust for maximum speed and reliability', 'fastdev', 'fastmcp', 'https://github.com/sussa3007/m2ysql-mcp', 'https://fastmcp.dev', false, 'Rust', 720, 180, 'MIT', NOW(), NOW()),
('Spring-MCP', 'Java Spring-based MCP server with enterprise-grade features', 'springdevs', 'spring-mcp', 'https://github.com/sussa3007/mysql4-mcp', 'https://spring-mcp.example.org', false, 'Java', 630, 150, 'Apache-2.0', NOW(), NOW()),
('PythonMCP', 'Pythonic implementation of MCP server with easy extensibility', 'pythonistas', 'python-mcp', 'https://github.com/sussa3007/mys3ql-mcp', 'https://python-mcp.example.com', false, 'Python', 580, 120, 'MIT', NOW(), NOW()),
('Go-MCP-Server', 'Lightweight Go implementation of MCP server focused on performance', 'gocoders', 'go-mcp-server', 'https://github.com/sussa3007/mysql2-mcp', 'https://go-mcp.example.io', false, 'Go', 520, 110, 'MIT', NOW(), NOW()),
('Clojure-MCP', 'Functional implementation of MCP server in Clojure', 'clojurians', 'clojure-mcp', 'https://github.com/sussa3007/my1sql-mcpp', 'https://clojure-mcp.example.com', false, 'Clojure', 320, 70, 'Eclipse Public License', NOW(), NOW()),
('RubyMCP', 'Ruby on Rails based MCP server with elegant API design', 'rubyists', 'ruby-mcp', 'https://github.com/rubyists/ruby-mcp', 'https://ruby-mcp.example.org', false, 'Ruby', 310, 65, 'MIT', NOW(), NOW()),
('ElixirMCP', 'Fault-tolerant MCP server built on Elixir and OTP', 'elixirteam', 'elixir-mcp', 'https://github.com/elixirteam/elixir-mcp', 'https://elixir-mcp.example.com', false, 'Elixir', 290, 60, 'Apache-2.0', NOW(), NOW()),
('ScalaMCP', 'Reactive MCP server implementation using Scala and Akka', 'scalateam', 'scala-mcp', 'https://github.com/scalateam/scala-mcp', 'https://scala-mcp.example.io', false, 'Scala', 280, 55, 'Apache-2.0', NOW(), NOW()),
('KotlinMCP', 'Kotlin implementation of MCP server with coroutines support', 'kotlindev', 'kotlin-mcp', 'https://github.com/kotlindev/kotlin-mcp', 'https://kotlin-mcp.example.com', false, 'Kotlin', 410, 90, 'Apache-2.0', NOW(), NOW()),
('PHPMCP', 'PHP-based MCP server implementation for web integration', 'phpteam', 'php-mcp', 'https://github.com/phpteam/php-mcp', 'https://php-mcp.example.org', false, 'PHP', 240, 50, 'MIT', NOW(), NOW()),
('RustMCPServer', 'Secure MCP server implementation with Rust', 'rustdevs', 'rust-mcp-server', 'https://github.com/rustdevs/rust-mcp-server', 'https://rust-mcp.example.io', false, 'Rust', 530, 115, 'MIT', NOW(), NOW()),
('CppMCP', 'High-performance C++ implementation of MCP server', 'cppteam', 'cpp-mcp', 'https://github.com/cppteam/cpp-mcp', 'https://cpp-mcp.example.com', false, 'C++', 380, 85, 'BSD-3-Clause', NOW(), NOW()),
('DotNetMCP', '.NET Core implementation of MCP server', 'dotnetdevs', 'dotnet-mcp', 'https://github.com/dotnetdevs/dotnet-mcp', 'https://dotnet-mcp.example.org', false, 'C#', 370, 80, 'MIT', NOW(), NOW()),
('TypeScriptMCP', 'TypeScript implementation of MCP server with strong typing', 'tsdevs', 'typescript-mcp', 'https://github.com/tsdevs/typescript-mcp', 'https://ts-mcp.example.io', false, 'TypeScript', 490, 105, 'MIT', NOW(), NOW()),
('SwiftMCP', 'Swift implementation of MCP server for Apple ecosystem', 'swiftteam', 'swift-mcp', 'https://github.com/swiftteam/swift-mcp', 'https://swift-mcp.example.com', false, 'Swift', 260, 55, 'Apache-2.0', NOW(), NOW()),
('DartMCP', 'Dart implementation of MCP server with Flutter compatibility', 'dartdevs', 'dart-mcp', 'https://github.com/dartdevs/dart-mcp', 'https://dart-mcp.example.org', false, 'Dart', 230, 50, 'MIT', NOW(), NOW()),
('HaskellMCP', 'Pure functional MCP server implementation in Haskell', 'haskellteam', 'haskell-mcp', 'https://github.com/haskellteam/haskell-mcp', 'https://haskell-mcp.example.io', false, 'Haskell', 200, 40, 'BSD-3-Clause', NOW(), NOW()),
('PerlMCP', 'Perl implementation of MCP server for legacy systems', 'sussa3007', 'mysql-mcp', 'https://github.com/sussa3007/mysql-mcp', 'https://perl-mcp.example.com', false, 'Perl', 150, 30, 'Artistic-2.0', NOW(), NOW());

-- 서버 레포지토리 태그 연결
INSERT INTO server_repository_tags (server_repository_id, tag_id, created_at, updated_at) VALUES
(1, 1, NOW(), NOW()), -- MCP-Server + mcp
(1, 2, NOW(), NOW()), -- MCP-Server + server
(1, 7, NOW(), NOW()), -- MCP-Server + go
(1, 12, NOW(), NOW()), -- MCP-Server + high-performance
(1, 21, NOW(), NOW()), -- MCP-Server + open-source
(2, 1, NOW(), NOW()), -- Node-MCP-Server + mcp
(2, 2, NOW(), NOW()), -- Node-MCP-Server + server
(2, 4, NOW(), NOW()), -- Node-MCP-Server + javascript
(2, 10, NOW(), NOW()), -- Node-MCP-Server + websocket
(3, 1, NOW(), NOW()), -- FastMCP + mcp
(3, 2, NOW(), NOW()), -- FastMCP + server
(3, 12, NOW(), NOW()), -- FastMCP + high-performance
(4, 1, NOW(), NOW()), -- Spring-MCP + mcp
(4, 2, NOW(), NOW()), -- Spring-MCP + server
(4, 6, NOW(), NOW()), -- Spring-MCP + java
(5, 1, NOW(), NOW()), -- PythonMCP + mcp
(5, 2, NOW(), NOW()), -- PythonMCP + server
(5, 8, NOW(), NOW()); -- PythonMCP + python

-- 서버 레포지토리 환경 변수
INSERT INTO environment_variables (server_repository_id, name, description, required, default_value, created_at, updated_at) VALUES
(1, 'MCP_API_KEY', 'API key for authentication', 1, NULL, NOW(), NOW()),
(1, 'MCP_HOST', 'Host address for the server', 1, '0.0.0.0', NOW(), NOW()),
(1, 'MCP_PORT', 'Port for the server to listen on', 0, '8080', NOW(), NOW()),
(1, 'MCP_LOG_LEVEL', 'Logging level (debug, info, warn, error)', 0, 'info', NOW(), NOW()),
(1, 'MCP_DB_URI', 'Database connection URI', 1, NULL, NOW(), NOW()),
(2, 'API_KEY', 'Authentication key for API access', 1, NULL, NOW(), NOW()),
(2, 'PORT', 'Server port number', 0, '3000', NOW(), NOW()),
(2, 'LOG_LEVEL', 'Logging verbosity', 0, 'info', NOW(), NOW()),
(3, 'FASTMCP_TOKEN', 'Security token for API', 1, NULL, NOW(), NOW()),
(3, 'FASTMCP_PORT', 'Server port', 0, '8000', NOW(), NOW());

-- 서버 레포지토리 명령어
INSERT INTO commands (server_repository_id, name, description, command, created_at, updated_at) VALUES
(1, 'Start Server', 'Start the MCP server in development mode', 'go run cmd/server/main.go', NOW(), NOW()),
(1, 'Build Server', 'Build the MCP server binary', 'go build -o mcp-server cmd/server/main.go', NOW(), NOW()),
(1, 'Run Tests', 'Run all tests', 'go test ./...', NOW(), NOW()),
(2, 'Start Dev Server', 'Start development server with hot reload', 'npm run dev', NOW(), NOW()),
(2, 'Build', 'Build production version', 'npm run build', NOW(), NOW()),
(2, 'Test', 'Run test suite', 'npm test', NOW(), NOW()),
(3, 'Start', 'Start the FastMCP server', 'cargo run --release', NOW(), NOW()),
(3, 'Build', 'Build optimized binary', 'cargo build --release', NOW(), NOW()),
(3, 'Test', 'Run test suite', 'cargo test', NOW(), NOW());

-- 서버 레포지토리 배포 옵션
INSERT INTO deployment_options (server_repository_id, name, description, instructions, created_at, updated_at) VALUES
(1, 'Docker', 'Deploy using Docker', 'docker run -p 8080:8080 mcp-organization/mcp-server:latest', NOW(), NOW()),
(1, 'Kubernetes', 'Deploy on Kubernetes', 'kubectl apply -f kubernetes/mcp-server.yaml', NOW(), NOW()),
(1, 'Binary', 'Run compiled binary', './mcp-server', NOW(), NOW()),
(2, 'Docker', 'Deploy using Docker', 'docker run -p 3000:3000 nodeteam/node-mcp-server', NOW(), NOW()),
(2, 'PM2', 'Deploy with PM2 process manager', 'pm2 start dist/index.js --name node-mcp', NOW(), NOW()),
(3, 'Docker', 'Deploy with Docker', 'docker run -p 8000:8000 fastdev/fastmcp', NOW(), NOW());

-- 서버 레포지토리 기여자 연결
INSERT INTO server_repository_contributors (server_repository_id, contributor_id, created_at, updated_at) VALUES
(1, 1, NOW(), NOW()),
(1, 2, NOW(), NOW()),
(1, 3, NOW(), NOW()),
(2, 4, NOW(), NOW()),
(2, 5, NOW(), NOW()),
(3, 1, NOW(), NOW());

-- MCP 도구 (서버 레포지토리에 연결)
INSERT INTO mcp_tools (server_repository_id, name, description, version, is_required, created_at, updated_at) VALUES
(1, 'MCP CLI', 'Command-line tool for interacting with MCP server', '1.2.0', true, NOW(), NOW()),
(1, 'MCP Admin Panel', 'Web-based admin interface for MCP management', '0.9.5', false, NOW(), NOW()),
(2, 'Node MCP API', 'HTTP API for Node MCP server integration', '1.0.0', true, NOW(), NOW()),
(3, 'FastMCP SDK', 'Software development kit for FastMCP integration', '0.8.0', true, NOW(), NOW());

-- 클라이언트 레포지토리 데이터
INSERT INTO client_repositories (name, description, owner, repo, github_url, is_official, language, stars, forks, license, created_at, updated_at) VALUES
('MCP-Client', 'Official Model Context Protocol client implementation with comprehensive features', 'mcp-organization', 'mcp-client', 'https://github.com/mcp-organization/mcp-client', true, 'TypeScript', 980, 260, 'MIT', NOW(), NOW()),
('React-MCP', 'React.js implementation of MCP client with component library', 'reactteam', 'react-mcp', 'https://github.com/reactteam/react-mcp', false, 'JavaScript', 750, 180, 'MIT', NOW(), NOW()),
('Vue-MCP-Client', 'Vue.js implementation of MCP client with intuitive UI', 'vueteam', 'vue-mcp-client', 'https://github.com/vueteam/vue-mcp-client', false, 'JavaScript', 680, 150, 'MIT', NOW(), NOW()),
('Angular-MCP', 'Angular implementation of MCP client with TypeScript integration', 'angulardevs', 'angular-mcp', 'https://github.com/angulardevs/angular-mcp', false, 'TypeScript', 540, 120, 'Apache-2.0', NOW(), NOW()),
('Flutter-MCP', 'Flutter implementation of MCP client for cross-platform mobile and desktop', 'flutterteam', 'flutter-mcp', 'https://github.com/flutterteam/flutter-mcp', false, 'Dart', 620, 140, 'MIT', NOW(), NOW()),
('Swift-MCP-Client', 'Swift implementation of MCP client for iOS and macOS', 'swiftteam', 'swift-mcp-client', 'https://github.com/swiftteam/swift-mcp-client', false, 'Swift', 480, 100, 'MIT', NOW(), NOW()),
('Kotlin-MCP', 'Kotlin implementation of MCP client for Android', 'kotlindevs', 'kotlin-mcp', 'https://github.com/kotlindevs/kotlin-mcp', false, 'Kotlin', 420, 90, 'Apache-2.0', NOW(), NOW()),
('Python-MCP-Client', 'Python-based MCP client library with asyncio support', 'pythonistas', 'python-mcp-client', 'https://github.com/pythonistas/python-mcp-client', false, 'Python', 380, 85, 'MIT', NOW(), NOW()),
('Electron-MCP', 'Electron-based desktop application for MCP client', 'electronteam', 'electron-mcp', 'https://github.com/electronteam/electron-mcp', false, 'JavaScript', 330, 70, 'MIT', NOW(), NOW()),
('Unity-MCP', 'Unity implementation of MCP client for gaming applications', 'unitydevs', 'unity-mcp', 'https://github.com/unitydevs/unity-mcp', false, 'C#', 290, 65, 'MIT', NOW(), NOW()),
('Svelte-MCP', 'Svelte implementation of MCP client with minimal bundle size', 'svelteteam', 'svelte-mcp', 'https://github.com/svelteteam/svelte-mcp', false, 'JavaScript', 260, 60, 'MIT', NOW(), NOW()),
('Go-MCP-Client', 'Go implementation of MCP client with concurrency support', 'goteam', 'go-mcp-client', 'https://github.com/goteam/go-mcp-client', false, 'Go', 240, 55, 'MIT', NOW(), NOW()),
('Ruby-MCP', 'Ruby implementation of MCP client with Rails integration', 'rubyteam', 'ruby-mcp', 'https://github.com/rubyteam/ruby-mcp', false, 'Ruby', 220, 50, 'MIT', NOW(), NOW()),
('C++-MCP-Client', 'C++ implementation of MCP client with high performance', 'cppteam', 'cpp-mcp-client', 'https://github.com/cppteam/cpp-mcp-client', false, 'C++', 210, 45, 'BSD-3-Clause', NOW(), NOW()),
('Rust-MCP-Client', 'Rust implementation of MCP client with memory safety', 'rustteam', 'rust-mcp-client', 'https://github.com/rustteam/rust-mcp-client', false, 'Rust', 200, 40, 'MIT', NOW(), NOW()),
('Next-MCP', 'Next.js implementation of MCP client with SSR support', 'nextteam', 'next-mcp', 'https://github.com/nextteam/next-mcp', false, 'TypeScript', 185, 35, 'MIT', NOW(), NOW()),
('PHP-MCP-Client', 'PHP implementation of MCP client for web applications', 'phpteam', 'php-mcp-client', 'https://github.com/phpteam/php-mcp-client', false, 'PHP', 170, 30, 'MIT', NOW(), NOW()),
('Java-MCP-Client', 'Java implementation of MCP client with Spring Boot integration', 'javateam', 'java-mcp-client', 'https://github.com/javateam/java-mcp-client', false, 'Java', 160, 25, 'Apache-2.0', NOW(), NOW()),
('DotNet-MCP', '.NET implementation of MCP client with MAUI support', 'dotnetteam', 'dotnet-mcp', 'https://github.com/dotnetteam/dotnet-mcp', false, 'C#', 150, 20, 'MIT', NOW(), NOW()),
('Ember-MCP', 'Ember.js implementation of MCP client with robust data management', 'sussa3007', 'mysql-mcp', 'https://github.com/emberteam/ember-mcp', false, 'JavaScript', 140, 15, 'MIT', NOW(), NOW());

-- 클라이언트 레포지토리 태그 연결
INSERT INTO client_repository_tag_mappings (client_repository_id, tag_id, created_at, updated_at) VALUES
(1, 1, NOW(), NOW()), -- MCP-Client + mcp
(1, 3, NOW(), NOW()), -- MCP-Client + client
(1, 5, NOW(), NOW()), -- MCP-Client + typescript
(1, 18, NOW(), NOW()), -- MCP-Client + web
(1, 21, NOW(), NOW()), -- MCP-Client + open-source
(2, 1, NOW(), NOW()), -- React-MCP + mcp
(2, 3, NOW(), NOW()), -- React-MCP + client
(2, 4, NOW(), NOW()), -- React-MCP + javascript
(2, 18, NOW(), NOW()), -- React-MCP + web
(3, 1, NOW(), NOW()), -- Vue-MCP-Client + mcp
(3, 3, NOW(), NOW()), -- Vue-MCP-Client + client
(3, 4, NOW(), NOW()), -- Vue-MCP-Client + javascript
(4, 1, NOW(), NOW()), -- Angular-MCP + mcp
(4, 3, NOW(), NOW()), -- Angular-MCP + client
(4, 5, NOW(), NOW()), -- Angular-MCP + typescript
(5, 1, NOW(), NOW()), -- Flutter-MCP + mcp
(5, 3, NOW(), NOW()), -- Flutter-MCP + client
(5, 16, NOW(), NOW()); -- Flutter-MCP + mobile

-- 클라이언트 레포지토리 기여자 연결
INSERT INTO client_repository_contributors (client_repository_id, contributor_id, created_at, updated_at) VALUES
(1, 1, NOW(), NOW()),
(1, 2, NOW(), NOW()),
(2, 3, NOW(), NOW()),
(2, 4, NOW(), NOW()),
(3, 5, NOW(), NOW());

-- 클라이언트 레포지토리 사용 예제
INSERT INTO client_repository_usage_examples (client_repository_id, usage_example) VALUES
(1, 'import { MCPClient } from "@mcp/client";\n\nconst client = new MCPClient({\n  serverUrl: "wss://example.com",\n  apiKey: "your-api-key"\n});\n\nclient.connect();'),
(1, 'client.on("message", (data) => {\n  console.log("Received:", data);\n});\n\nclient.send("Hello MCP!");'),
(2, 'import { useMCP } from "@mcp/react-client";\n\nfunction App() {\n  const { connect, send } = useMCP();\n  return <button onClick={() => send("Hello")}>Send</button>;\n}'),
(3, 'import { createMCP } from "@mcp/vue-client";\n\nconst mcp = createMCP({\n  serverUrl: "wss://example.com"\n});\n\nexport default { setup() { return { mcp }; } }');

-- 클라이언트 레포지토리 지원 언어
INSERT INTO client_repository_supported_languages (client_repository_id, supported_language) VALUES
(1, 'JavaScript'),
(1, 'TypeScript'),
(2, 'JavaScript'),
(2, 'TypeScript'),
(3, 'JavaScript');

-- 클라이언트 레포지토리 플랫폼
INSERT INTO client_repository_platforms (client_repository_id, platform) VALUES
(1, 'Browser'),
(1, 'Node.js'),
(1, 'Deno'),
(2, 'Browser'),
(2, 'Node.js'),
(3, 'Browser'),
(3, 'Node.js');

-- 제출 데이터
INSERT INTO submissions (name, author, repository_id, type, description, repo_url, website_url, email, status, message, created_at, updated_at) VALUES
('FastAPI MCP Server', 'David Johnson', 1, 'SERVER', 'A high-performance MCP server implementation using FastAPI and asyncio', 'https://github.com/davidj/fastapi-mcp-server', 'https://fastapi-mcp.example.com', 'david@example.com', 'PENDING', NULL, NOW(), NOW()),
('React Native MCP Client', 'Sarah Parker', 1, 'CLIENT', 'React Native implementation of MCP client for mobile applications', 'https://github.com/sarahp/react-native-mcp', 'https://reactnative-mcp.example.org', 'sarah@example.com', 'APPROVED', 'Great implementation with comprehensive documentation!', NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY),
('Scala MCP Server', 'Michael Brown', 2, 'SERVER', 'Scala-based MCP server with Akka streams', 'https://github.com/mikeb/scala-mcp-server', 'https://scala-mcp-server.example.io', 'mike@example.com', 'REJECTED', 'Missing key MCP protocol features. Please update and resubmit.', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 3 DAY),
('MCP.js Client', 'Emma Wilson', 2, 'CLIENT', 'Lightweight JavaScript client library for MCP', 'https://github.com/emmaw/mcp.js', 'https://mcpjs.example.com', 'emma@example.com', 'APPROVED', 'Excellent minimal client implementation!', NOW() - INTERVAL 10 DAY, NOW() - INTERVAL 8 DAY),
('Django MCP Server', 'Thomas Lee', 3, 'SERVER', 'Django-based MCP server with REST framework', 'https://github.com/thomasl/django-mcp', 'https://django-mcp.example.org', 'thomas@example.com', 'PENDING', NULL, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),
('SwiftUI MCP Client', 'Olivia Garcia', 3, 'CLIENT', 'SwiftUI implementation of MCP client for iOS', 'https://github.com/oliviag/swiftui-mcp', 'https://swiftui-mcp.example.io', 'olivia@example.com', 'APPROVED', 'Beautiful UI and excellent performance!', NOW() - INTERVAL 15 DAY, NOW() - INTERVAL 12 DAY),
('Ktor MCP Server', 'James Wilson', 4, 'SERVER', 'Kotlin Ktor-based MCP server with coroutines', 'https://github.com/jamesw/ktor-mcp', 'https://ktor-mcp.example.com', 'james@example.com', 'REJECTED', 'Implementation does not follow the MCP specification correctly.', NOW() - INTERVAL 20 DAY, NOW() - INTERVAL 18 DAY),
('Svelte MCP UI Kit', 'Sophie Martinez', 4, 'CLIENT', 'Svelte-based UI component library for MCP clients', 'https://github.com/sophiem/svelte-mcp-ui', 'https://svelte-mcp-ui.example.org', 'sophie@example.com', 'PENDING', NULL, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),
('Express MCP Server', 'Daniel Thompson', 5, 'SERVER', 'Express.js implementation of MCP server', 'https://github.com/danielt/express-mcp', 'https://express-mcp.example.io', 'daniel@example.com', 'APPROVED', 'Well-structured code and excellent documentation!', NOW() - INTERVAL 25 DAY, NOW() - INTERVAL 22 DAY),
('Vue3 MCP Components', 'Isabella Kim', 5, 'CLIENT', 'Vue 3 component library for MCP', 'https://github.com/isabellak/vue3-mcp-components', 'https://vue3-mcp.example.com', 'isabella@example.com', 'APPROVED', 'Comprehensive component set with great TypeScript support.', NOW() - INTERVAL 30 DAY, NOW() - INTERVAL 27 DAY),
('Quarkus MCP Server', 'Ethan Davis', 6, 'SERVER', 'Quarkus-based MCP server with native compilation', 'https://github.com/ethand/quarkus-mcp', 'https://quarkus-mcp.example.org', 'ethan@example.com', 'PENDING', NULL, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY),
('Angular MCP Dashboard', 'Ava Robinson', 7, 'CLIENT', 'Angular-based admin dashboard for MCP servers', 'https://github.com/avar/angular-mcp-dashboard', 'https://angular-mcp-dashboard.example.io', 'ava@example.com', 'REJECTED', 'UI has significant accessibility issues. Please fix and resubmit.', NOW() - INTERVAL 35 DAY, NOW() - INTERVAL 32 DAY),
('Phoenix MCP Server', 'Noah Taylor', 8, 'SERVER', 'Elixir Phoenix-based MCP server', 'https://github.com/noaht/phoenix-mcp', 'https://phoenix-mcp.example.com', 'noah@example.com', 'APPROVED', 'Excellent fault-tolerance and scaling capabilities!', NOW() - INTERVAL 40 DAY, NOW() - INTERVAL 37 DAY),
('React MCP Admin', 'Mia Clark', 8, 'CLIENT', 'React-based admin interface for MCP servers', 'https://github.com/miac/react-mcp-admin', 'https://react-mcp-admin.example.org', 'mia@example.com', 'PENDING', NULL, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY),
('Actix MCP Server', 'William Rodriguez', 9, 'SERVER', 'Rust Actix-based MCP server', 'https://github.com/williamr/actix-mcp', 'https://actix-mcp.example.io', 'william@example.com', 'APPROVED', 'Outstanding performance and memory safety!', NOW() - INTERVAL 45 DAY, NOW() - INTERVAL 42 DAY),
('Blazor MCP Client', 'Charlotte White', 9, 'CLIENT', '.NET Blazor-based MCP client', 'https://github.com/charlottew/blazor-mcp', 'https://blazor-mcp.example.com', 'charlotte@example.com', 'REJECTED', 'Implementation misses key client features. Please revise.', NOW() - INTERVAL 50 DAY, NOW() - INTERVAL 47 DAY),
('Micronaut MCP Server', 'Benjamin Hall', 10, 'SERVER', 'Java Micronaut-based MCP server', 'https://github.com/benjaminh/micronaut-mcp', 'https://micronaut-mcp.example.org', 'benjamin@example.com', 'PENDING', NULL, NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY),
('Preact MCP Components', 'Amelia Lopez', 10, 'CLIENT', 'Preact-based lightweight component library for MCP', 'https://github.com/amelial/preact-mcp', 'https://preact-mcp.example.io', 'amelia@example.com', 'APPROVED', 'Impressive bundle size optimization!', NOW() - INTERVAL 55 DAY, NOW() - INTERVAL 52 DAY),
('Gin MCP Server', 'Henry Nguyen', 11, 'SERVER', 'Go Gin-based MCP server', 'https://github.com/henryn/gin-mcp', 'https://gin-mcp.example.com', 'henry@example.com', 'REJECTED', 'Performance issues under high load. Please optimize and resubmit.', NOW() - INTERVAL 60 DAY, NOW() - INTERVAL 57 DAY),
('Solid.js MCP UI', 'Evelyn Scott', 11, 'CLIENT', 'Solid.js-based MCP client UI', 'https://github.com/evelyns/solidjs-mcp', 'https://solidjs-mcp.example.org', 'evelyn@example.com', 'PENDING', NULL, NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY);

-- 제출 태그 연결
INSERT INTO submission_tags (submission_id, tag_id, created_at, updated_at) VALUES
(1, 1, NOW(), NOW()), -- FastAPI MCP Server + mcp
(1, 2, NOW(), NOW()), -- FastAPI MCP Server + server
(1, 8, NOW(), NOW()), -- FastAPI MCP Server + python
(1, 12, NOW(), NOW()), -- FastAPI MCP Server + high-performance
(2, 1, NOW(), NOW()), -- React Native MCP Client + mcp
(2, 3, NOW(), NOW()), -- React Native MCP Client + client
(2, 4, NOW(), NOW()), -- React Native MCP Client + javascript
(2, 16, NOW(), NOW()), -- React Native MCP Client + mobile
(3, 1, NOW(), NOW()), -- Scala MCP Server + mcp
(3, 2, NOW(), NOW()), -- Scala MCP Server + server
(3, 11, NOW(), NOW()), -- Scala MCP Server + real-time
(4, 1, NOW(), NOW()), -- MCP.js Client + mcp
(4, 3, NOW(), NOW()), -- MCP.js Client + client
(4, 4, NOW(), NOW()), -- MCP.js Client + javascript
(4, 22, NOW(), NOW()), -- MCP.js Client + lightweight
(5, 1, NOW(), NOW()), -- Django MCP Server + mcp
(5, 2, NOW(), NOW()), -- Django MCP Server + server
(5, 8, NOW(), NOW()); -- Django MCP Server + python

-- 추가 클라이언트 레포지토리 기여자 연결
INSERT INTO client_repository_contributors (client_repository_id, contributor_id, created_at, updated_at) VALUES
(4, 1, NOW(), NOW()),
(4, 3, NOW(), NOW()),
(5, 2, NOW(), NOW()),
(5, 4, NOW(), NOW()),
(6, 5, NOW(), NOW()),
(6, 1, NOW(), NOW());

-- 추가 클라이언트 레포지토리 사용 예제
INSERT INTO client_repository_usage_examples (client_repository_id, usage_example) VALUES
(4, 'import { MCPModule } from "@mcp/angular";\n\n@NgModule({\n  imports: [MCPModule.forRoot({\n    serverUrl: "wss://example.com",\n    apiKey: "your-api-key"\n  })]\n})\nexport class AppModule { }'),
(4, '@Component({\n  selector: "app-root",\n  template: `<mcp-connection [onMessage]="handleMessage"></mcp-connection>`\n})\nexport class AppComponent {\n  handleMessage(data: any) {\n    console.log("Received:", data);\n  }\n}'),
(5, 'import "package:flutter_mcp/mcp.dart";\n\nfinal client = MCPClient(\n  serverUrl: "wss://example.com",\n  apiKey: "your-api-key"\n);\n\nvoid main() {\n  client.connect();\n}'),
(6, 'import MCPClient\n\nlet client = MCPClient(\n    serverURL: URL(string: "wss://example.com")!,\n    apiKey: "your-api-key"\n)\n\nclient.connect()');

-- 추가 클라이언트 레포지토리 지원 언어
INSERT INTO client_repository_supported_languages (client_repository_id, supported_language) VALUES
(4, 'TypeScript'),
(5, 'Dart'),
(5, 'TypeScript'),
(6, 'Swift'),
(6, 'Objective-C');

-- 추가 클라이언트 레포지토리 플랫폼
INSERT INTO client_repository_platforms (client_repository_id, platform) VALUES
(4, 'Browser'),
(4, 'Electron'),
(5, 'iOS'),
(5, 'Android'),
(5, 'Windows'),
(5, 'macOS'),
(5, 'Linux'),
(6, 'iOS'),
(6, 'macOS'),
(6, 'watchOS'),
(6, 'tvOS'); 