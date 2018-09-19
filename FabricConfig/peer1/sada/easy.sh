
IF_COUCHDB="$1"
echo "为 network.sh 赋权限"
chmod u+x network.sh
if [ "${IF_COUCHDB}" == "cdb" ]; then
echo "使用 couchDB 启动网络"
   ./network.sh up cdb 
else
echo "不使用 couchDB 启动网络"
   ./network.sh up  
fi
