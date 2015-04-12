# mongoimport -d AmazonReviews -c watches Watches.json
# start mongo
#Commands in mongo
# use AmazonReviews
# db.watches.find({"product/productId" : "B000NLZ4AM"},{"review/text":1},{"_id":0})

import gzip 
import json as simplejson 


def parse(filename):
    f = gzip.open(filename, 'r')
    entry = {}
    for l in f: 
        l = l.strip() 
        colonPos = l.find(':')
        if colonPos == -1: 
            yield entry 
            entry = {} 
            continue
        eName = l[:colonPos] 
        rest = l[colonPos+2:] 
        entry[eName] = rest 
    yield entry


productReview={}
for e in parse("Watches.txt.gz"):
    if "product/productId" not in e: continue
    if e["product/productId"] not in productReview: productReview[e["product/productId"]]=[e]
    else: 
        reviews=productReview[e["product/productId"]]
        reviews.append(e)
        productReview[e["product/productId"]]=reviews
for e in parse("Watches.txt.gz"):
    if "product/productId" not in e: continue
    reviews=productReview[e["product/productId"]]
    if len(reviews) > 3:
        print(simplejson.dumps(e))

