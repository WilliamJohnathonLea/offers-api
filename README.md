# Offers API

## Creating an offer

| Param        | Description |
| :----        | :---------- |
| desc         | A friendly description of the offer |
| price        | The price of the offer in the currency's smallest denomination e.g. pence |
| currency     | The currency for the price of the offer |
| expiresAfter | The time in epoch seconds which an offer will expire after e.g. 86400 (1 day) |

### Request
```
POST /api/offers

{
    "desc" : "A friendly description",
    "price" : 100,
    "currency" : "GBP",
    "expiresAfter" : 86400
}
```

### Response
```
201 Created

{
    "_id" : "123abc456def..."
    "desc" : "A friendly description",
    "price" : 100,
    "currency" : "GBP",
    "dateCreated" : 1523232000,
    "expiresAfter" : 86400,
    "hasExpired" : false
}
```

## Retrieving an offer

### Request
```
GET /api/offers/123abc456def
```

### Response
```
200 OK

{
    "_id" : "123abc456def..."
    "desc" : "A friendly description",
    "price" : 100,
    "currency" : "GBP",
    "dateCreated" : 1523232000,
    "expiresAfter" : 86400,
    "hasExpired" : false
}
```

## Expiring an offer

| Param        | Description |
| :----        | :---------- |
| expire       | The command to expire the offer. (must be `true`) |

### Request
```
PATCH /api/offers/123abc456def

{
    "expire" : true
}
```

### Response
```
204 No Content
```

## Auto expiry of offers
For the sake of brevity I have not added in the feature of auto-expiry. But in order to implement this feature I would create
a scheduler to run a job every so often to check the offers creation dates and expiry times. This job would update the jobs
to say they are expired if the current date is passed the job's expiry time.

Either that, or update the offer when a GET request is sent for it. This would have the advantage of not running a potentially
large database query.
