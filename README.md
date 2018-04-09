# Offers API

## Creating an offer

| Param        | Description |
| :---:        | :---------: |
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