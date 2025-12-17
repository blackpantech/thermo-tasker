# Thermo Tasker

> *The hottest TODO list*

## What is Thermo Tasker?

Thermo Tasker is an innovative task management application that bridges digital workflow with physical output. Make your TODO lists more tangible and satisfying.

> Notifications and TODO apps are useful and help us be more productive but phones are too distracting.

By printing tasks on thermal paper, we maximize user engagement but minimize the distraction of notifications on your phone.

This project combines a modern web interface for task creation, visualization, and planning with automated thermal printer integration, making task management both digital and tactile.

## Architecture

Thermo Tasker follows a distributed and event-driven architecture, designed for flexibility, fault tolerance, and maintainability. The system is built with:
- **Hexagonal Architecture**: Clean separation of concerns with domain logic isolated from external dependencies
- **Event-Driven Design**: Decoupled services communicate through events, enabling independent scaling and deployment
- **Test-Driven Development (TDD)**: Comprehensive test coverage ensures reliability
- **Docker Containerization**: All services are containerized for consistent deployment across environments and ease of deployment

Key components include:
- **Web Interface**: static Thymeleaf templates for task creation, visualization, and print scheduling
- **API Service**: RESTful backend handling task management, persistence and print orchestration
- **Event Bus**: Message broker coordinating asynchronous operations between services
- **Printer Service**: Specialized microservice managing thermal printer communication

## Dependencies

- Docker & Docker Compose
- Thymeleaf (for web interface)
- Java 25 and Spring (for API and printer service)
- PostgreSQL 16 (task persistence)
- RabbitMQ 4 (event messaging)

## Getting Started with Docker Compose

### Prerequisites
- Docker and Docker Compose installed on your system

### Running the Application

1. Connect the printer to your machine and add your user to the `lp` group:
```bash
usermod -a -G lp $USER
```

2. Create a repository for the configuration files:
```bash
mkdir thermo-tasker
cd thermo-tasker
```

3. Get the config files and edit `example.env`, `definitions.json` and `init.sql` files as needed for your environment:
```bash
wget -O docker-compose.yml https://github.com/blackpantech/thermo-tasker/releases/latest/download/docker-compose.yml
wget -O .env https://github.com/blackpantech/thermo-tasker/releases/latest/download/example.env
mkdir init
wget -O init/definitions.json https://github.com/blackpantech/thermo-tasker/releases/latest/download/definitions.json
wget -O init/init.sql https://github.com/blackpantech/thermo-tasker/releases/latest/download/init.sql
wget -O init/rabbitmq.conf https://github.com/blackpantech/thermo-tasker/releases/latest/download/rabbitmq.conf
```

4. Start all services with Docker Compose:
```bash
docker compose pull && docker compose up -d
```

5. Access the web interface at `http://localhost:8080`

6. Monitor services:
```bash
docker compose logs -f
```

7. Stop all services:
```bash
docker compose down
```

### Service Endpoints
- **Web Interface and API service**: http://localhost:8080
- **RabbitMQ Management**: http://localhost:15672 (by default: guest/guest)

## Notes
I could only test the application with an Epson TM-T20III thermal printer so check the compatibility of your printer beforehand.
