#TODO:
* Loading View.
* Error views: Unexpected/Network/User not found.
* Handle repositories pagination (Bitbucket) - https://developer.atlassian.com/bitbucket/api/2/reference/resource/repositories/%7Busername%7D .
* Implement Team Foundation Server - https://www.visualstudio.com/en-us/docs/integrate/get-started/auth/oauth#get-an-access-token .
* Implement main repository file listing.
* Implement readme.
* Try to use reactive to make refreshTokens requests (for example MergeDelayError - http://reactivex.io/documentation/operators/merge.html)
* Try to use reactive to chain the two requests ExchangeToken -> GetOwner.