import json
import boto3
from decimal import Decimal

class DecimalEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return float(obj)
        return super(CustomJsonEncoder, self).default(obj)
        
def lambda_handler(event, context):
    # Get the service resource.
    dynamodb = boto3.resource('dynamodb')
    table = dynamodb.Table('test_suggestrip')
    city_id = event["queryStringParameters"]['ID']
    response = table.get_item(Key={'ID': city_id})
    item = response['Item']
    #print(item)

    
    return {
        'statusCode': 200,
        'body': json.dumps(item, cls=DecimalEncoder)
    }
