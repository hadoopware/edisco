#!/bin/bash
export GOOGLE_APPLICATION_CREDENTIALS=edisko-a23c7be2b230.json

curl -s -X POST -H "Content-Type: application/json" \
    -H "Authorization: Bearer "$(gcloud auth application-default print-access-token) \
    --data "{
  'q': 'While living in Tokyo, I came to realize it has a higher concentration of companies than I expected—making it easier to conduct business—as well as many excellent engineers',
  'source': 'en',
  'target': 'ja',
  'format': 'text'
}" "https://translation.googleapis.com/language/translate/v2"

exit $?
#=================================================================
curl -s -X POST -H 'Content-Type: application/json' \
    -H 'Authorization: Bearer ya29.c.Elr3BE_zH4s3Y8Xs-XabJA2i4GhPkCWuj4kpGg7weznTcwozu-uLMBCBZwdx_wqQhjjw2hi2ehSkQnRZjwprmh8JH_EkNrXV3Ew5T_PQoEcmtzJDuTp3G-tOP3U' \
    'https://translation.googleapis.com/language/translate/v2' \
    -d @translate-request.json
