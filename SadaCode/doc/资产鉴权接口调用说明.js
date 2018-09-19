//比对关键信息
//上传文件完成后 -> 获取上传的文件的关键信息并与区块链的关键信息进行比对信息
$.post('${pageContext.request.contextPath}/assetAction_getFileKeyInfoAndCompareKeyInfo.do',
 {
 	'id': id,//此id为要鉴权的资产的id 
 	'fileName': fileName,//新上传的文件上传成功后返回的fileName
  	'filePath': filePath,//新上传的文件上传成功后返回的filePath
  },
 function(data, textStatus, xhr) {
	//返回结果示例{"code":null,"data":"{\"nKeyInfo\":\"{\\\"住址\\\":\\\"河北省石家庄市北二环17号8栋5单元103室\\\",\\\"公民身份号码\\\":\\\"130103199406055330\\\",\\\"出生\\\":\\\"19940605\\\",\\\"姓名\\\":\\\"小西\\\",\\\"性别\\\":\\\"男\\\",\\\"民族\\\":\\\"汉\\\"}\",\"compareResult\":\"{\\\"姓名\\\":\\\"yes\\\",\\\"民族\\\":\\\"yes\\\",\\\"住址\\\":\\\"yes\\\",\\\"公民身份号码\\\":\\\"no\\\",\\\"出生\\\":\\\"yes\\\",\\\"性别\\\":\\\"yes\\\"}\",\"chainAssetKeyInfo\":\"{\\\"住址\\\":\\\"河北省石家庄市北二环17号8栋5单元103室\\\",\\\"公民身份号码\\\":\\\"130103199406055250\\\",\\\"出生\\\":\\\"19940605\\\",\\\"姓名\\\":\\\"小西\\\",\\\"性别\\\":\\\"男\\\",\\\"民族\\\":\\\"汉\\\",}\"}","message":null,"success":true}
	//data.success为请求是否成功
	//data.data为返回的数据
	//		|-data.data.nKeyInfo 为新上传的资产的关键信息的json字符串  
	// 			|-取值方式：data.data.nKeyIndo.公民身份号码
	// 		|-data.data.chainAssetKeyInfo 为区块链中该资产的关键信息的json字符串
	//		|-data.data.compareResult 为比对结果的json字符串， 
	// 			|-取值方式：data.data.compareResult.公民身份号码
	// 				|-值为yes说明数据一致，no为数据不一致
	//
});

//比对MD5
//
$.post('${pageContext.request.contextPath}/assetAction_compareAssetFileMd5.do',
 {
 	'id': id,//此id为要鉴权的资产的id 
 	'fileName': fileName,//新上传的文件上传成功后返回的fileName
  	'filePath': filePath,//新上传的文件上传成功后返回的filePath
  },
 function(data, textStatus, xhr) {
	//返回结果示例{"code":null,"data":"{\"nMd5\":\"2cf9f5461df46d39c9ae66ee5b87921c\",\"compareResult\":\"no\",\"chainAssetMd5\":\"abcbf9178c3aae1e820af847b8984c10\"}","message":null,"success":true}
	//data.success为请求是否成功
	//data.data为返回的数据
	//		|-data.data.nMd5 为新上传的资产文件的Md5
	// 		|-data.data.chainAssetMd5 为区块链中该资产的Md5值
	//		|-data.data.compareResult 为比对结果
	// 				|-值为yes说明数据一致，no为数据不一致
	//
});
