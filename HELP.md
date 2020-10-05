# Getting Started

##简要说明
使用nginx配置文件下载服务，
但是nginx在windows下对中文支持有问题（系统默认gbk，nginx给网页端的数据为utf8），
因此导致之际额使用nginx作为文件下载服务时，中文文件不支持下载

为了弥补nginx在中文文件上的问题，
本服务使用springboot进行文件管理，
将文件目录扫描并传给前端；
前端获取到文件后，重定向到nginx服务进行下载