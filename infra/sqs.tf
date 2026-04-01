resource "aws_sqs_queue" "sqs_queue" {
  name                      = "image-processor-queue"
  receive_wait_time_seconds = 20
}

resource "aws_sqs_queue_policy" "queue-policy" {
  queue_url = aws_sqs_queue.sqs_queue.id

  policy = jsonencode({
    Version = "2012-10-17" 
    Statement = [{
      Sid    = "queue-policy-id"
      Effect = "Allow"
      Principal = {
        Service = "s3.amazonaws.com"
      }
      Action   = "SQS:SendMessage"
      Resource = aws_sqs_queue.sqs_queue.arn
    }]
  })
}