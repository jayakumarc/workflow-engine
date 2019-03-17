# Create a entity process
cat <<EOF | curl -H "Content-Type: application/json" -X POST -d @- http://localhost:8080/api/process-entities
{
    "entityId": "entity-1",
    "userId": "user-1",
    "type": "approval",
    "url": "http://localhost:4200/entity-detail/entity-1",
    "date": "2019-03-20"
}
EOF

# Update a entity process
cat <<EOF | curl -H "Content-Type: application/json" -X PUT -d @- http://localhost:8080/api/process-entities/entity-1
{
    "approved" : true
}
EOF