import json
import boto3
from botocore.exceptions import ClientError

def generate_presigned_url(s3_client, bucket, key, expires_in):
   url = s3_client.generate_presigned_url('put_object', Params={
        'Bucket': bucket,
        'Key': key,
        'ContentType': 'image/jpg', 
        }, ExpiresIn=expires_in)
   
   return {"statusCode": 200, "body": json.dumps({"url": url})}

def lambda_handler(event, context):
   try:
    print(event)
    body = json.loads(event['body'])
    userName = body.get('userName')
    fileName = body.get('fileName')

    s3_client = boto3.client("s3", region_name="ap-south-1")

    # The presigned URL is specified to expire in 1000 seconds
    return generate_presigned_url(s3_client, "image-storage-1853", userName+"/"+fileName, 1000)
   except ClientError:
        print(f"Couldn't get a presigned URL for client method.")
        raise