# ProductProfiler

## Project Description

Product reviews are the most influential source of information that help consumers in informed decision making. However, finding opinion about specific features of a product is often time consuming. Our project aims to extract relevant features of a product from its reviews and provide a consolidated opinion on each of them. For example, the system would provide an aggregated sentiment about individual features like audio, display, battery life for a mobile phone model without the user having to read all the reviews.

## Challenges

The overall sentiment about a product can be easily determined using text classification approaches or by rating 
aggregation. However, the challenges are identifying the most relevant features from customer reviews and extracting feature level opinions.

## Beneficiaries

The beneficiaries are product manufacturers and its consumers. Consumers will benefit from a granular feature level opinion which will aid them in quick and effortless decision making. Manufacturers will get a summarized feature level.

## Data sources

#### 1. Collection of reviews : http://snap.stanford.edu/data/web-Amazon-links.html
#### 2. Amazon Product Advertising API for product reviews
#### 3. eBay Developer API for product reviews

## Proposed Solution

For a given product we plan to identify potential features, which would involve resolution of anaphora like “it” and synonym identification for features. A ranking algorithm will then extract the most relevant set of features. This will be followed by clustering of comments about each feature. Finally an aggregated sentiment analysis for each feature will be performed based on these comments.

## Specific Techniques

#### 1. Feature extraction: Using shallow dependency parsing.
#### 2. Anaphora resolution: Either on our own or using open source tools like BART.
#### 3. Synonym Identification: Using WordNet/DBPedia.
#### 4. Feature Selection: Rank and select features using a relevancy algorithm like tf-idf.
#### 5. Sentiment classification of features: Using perceptron based on training data.

## Evaluation 

The system is evaluated on two aspects:
#### 1. Extraction of relevant features from product reviews
#### 2. Identifying the aggregated sentiment for each of the extracted features
