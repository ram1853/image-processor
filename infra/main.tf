terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "6.34.0"
    }
  }

  backend "s3" {
    bucket = "image-processor-tfstate-1853"
    key = "terraform.tfstate"
    region = "ap-south-1"
  }
} 

provider "aws" {
  region = "ap-south-1"
}
