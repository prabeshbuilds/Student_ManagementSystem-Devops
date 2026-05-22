🚀 Student Management System (DevOps Project)

A Spring Boot-based Student Management System integrated with modern DevOps tools for CI/CD, containerization, and automated deployment using Docker and Docker Compose.

📌 Project Overview

This project demonstrates a full DevOps workflow:

Java Spring Boot application
Maven build automation
Docker containerization
Docker Compose orchestration
Git version control with feature branching
Continuous integration-ready structure

🛠️ Tech Stack
☕ Java 17+ (Spring Boot)
📦 Maven
🐳 Docker
🐙 Docker Compose
🌿 Git & GitHub
🖥️ VS Code / Terminal
📂 Project Structure
student-management-system/
│── src/
│── target/
│── Dockerfile
│── docker-compose.yml
│── pom.xml
│── README.md

⚙️ How to Build & Run
1️⃣ Clone Repository
git clone https://github.com/<your-username>/student-management-system.git
cd student-management-system
2️⃣ Build the Project (Maven)
mvn clean package -DskipTests
3️⃣ Run with Docker Compose
docker compose up -d --build

This will:

Build the application image
Start containers in detached mode
4️⃣ Check Running Containers
docker ps -a
🔄 Git Workflow Used

This project follows a simple feature-based workflow:

git add .
git commit -m "updated feature"
git push origin cd

Branch used:

cd → Continuous Deployment / feature branch
🐳 Docker Commands Used
docker compose up -d --build   # Build and start containers
docker ps -a                   # List containers
docker start <container_name>  # Start stopped container
📦 Build Automation Flow
Code changes made in VS Code
Changes committed to GitHub
Maven builds .jar file
Docker builds image using Dockerfile
Docker Compose runs application container


Add Jenkins CI/CD pipeline
Integrate SonarQube for code quality
Add Prometheus + Grafana monitoring
Deploy on AWS EC2 / Kubernetes

 Author

Prabesh User
DevOps & Backend Learning Project

📜 License

This project is for learning purposes.
