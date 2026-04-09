# Image Processor

This repository contains an end-to-end image processing system that includes infrastructure provisioning, Kubernetes deployment, a Spring Boot worker service, and supporting utilities.

---

## Repository Structure

```
.
├── .github/               # GitHub Actions workflows (CI/CD)
├── business/              # Client script to upload images
├── functions/             # Python AWS Lambda functions
├── infra/                 # Terraform scripts for infrastructure provisioning
├── k8Definitions/         # Kubernetes manifests
├── worker/                # Spring Boot image processing service
└── README.md
```

---

## Folder Details

### `.github/`

Contains GitHub Actions workflows.
On every push to the `main` branch, the workflow:

* Provisions infrastructure using Terraform
* Builds Docker image
* Pushes image to AWS ECR
* Configures `kubectl`
* Creates Kubernetes `ServiceAccount`
* Creates/updates Kubernetes `Deployment`

---

### `business/`

Contains client-side scripts used to upload images into the system for processing.

---

### `functions/`

Contains Python code for AWS Lambda functions that generates URL for the clients to upload.

---

### `infra/`

Terraform scripts that provision:

* API Gateway
* Lambda
* IAM Roles
* Networking
* EKS Cluster
* ECR repositories
* Event Bridge Rules
* SQS
* Kubernetes dependencies

---

### `k8Definitions/`

Kubernetes resource definitions including:

* Deployment
* ServiceAccount
* Configurations required for running the worker

---

### `worker/`

Spring Boot application responsible for image processing.

#### Package Structure

```
com.image.processor
│
├── common
│   ├── dtos
│   ├── configs
│   ├── exceptions
│   └── utils
│
├── handler
│   └── Entry point for processing
│
├── helper
│   └── Image processing logic
│
└── service
    └── Orchestration layer
```

#### Tests

JUnit test cases are located in:

```
worker/src/test
```

---

## Building Docker Image Locally

Inside the `worker` folder:

```
cd worker
./build-docker-image.sh
```

This builds the Docker image locally.
This step is **optional** when using GitHub Actions.

---

## CI/CD Flow

When code is pushed to `main`:

1. GitHub Actions workflow starts
2. Terraform provisions/updates infrastructure
3. Docker image is built
4. Image is pushed to AWS ECR
5. `kubectl` is configured
6. Kubernetes `ServiceAccount` is created/updated
7. Kubernetes `Deployment` is applied

---

## High Level Architecture

```
Client (business script)
        │
        ▼
Upload Image
        │
        ▼
Kubernetes Worker (Spring Boot)
        │
        ▼
Image Processing Logic
        │
        ▼
Processed Output
```

---

## Prerequisites (for local development)

* Java 17+
* Maven
* Docker
* AWS CLI configured
* kubectl
* Terraform

---

## Notes

* Infrastructure is fully managed through Terraform
* Deployment is fully automated via GitHub Actions
* Local Docker build is only required for testing

---
