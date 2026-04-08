#Event Bridge Notification has to be turned on from the s3 side.
module "eventbridge" {
  source = "terraform-aws-modules/eventbridge/aws"
  create_bus = false
  bus_name = "default"

  rules = {
    image-created-event = {
      description = "Capture image creation event in s3"
      event_pattern = jsonencode({
        "source": ["aws.s3"],
        "detail-type": ["Object Created"],
        "detail": {
          "bucket": {
            "name": ["image-storage-1853"]
          },
          "object": {
            "key": [{
              "anything-but": {
                "wildcard": ["*medium*", "*small*"]
              }
            }]
          }
        }
      })
    }
  }

  targets = {
    image-created-event = [
      {
        name = "send-image-created-event-to-sqs"
        arn  = aws_sqs_queue.sqs_queue.arn
      }
    ]
  }
}