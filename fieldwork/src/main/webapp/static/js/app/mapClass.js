var mapClass={};//开辟命名空间，创建一个类
mapClass.ownMap=null;//地图实例

/*==========================================初始化地图--------------------------------------------*/
mapClass.init=function(address,number){
    mapClass.ownMap=new BMap.Map("mapContainer");
    mapClass.ownMap.centerAndZoom(address,number);
    mapClass.ownMap.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_LEFT}));  //左下角，仅包含平移按钮
    mapClass.ownMap.enableScrollWheelZoom();
};

/*========================================添加客户覆盖物--------------------------------------------*/

//客户覆盖物单个方法
mapClass.addClientMarker=function(city,clientObj,iconSrc){
		//city是改客户所在的城市
		var myIcon = new BMap.Icon(iconSrc, new BMap.Size(39,25));
		if(clientObj.latitude){
			var clientMarker=new BMap.Marker(new BMap.Point(clientObj.longitude,clientObj.latitude),{icon:myIcon});
	        mapClass.ownMap.addOverlay(clientMarker);
	        //创建信息窗口
	        var infoWindow = new BMap.InfoWindow("<ul style='padding:0px'><li><label class='labelClientTitle'>客户名称：</label ><span class='spanText'>"+clientObj.customerName+"</span></li><li><label class='labelClientTitle'>客户地址：</label><span class='spanText'>"+clientObj.customerAddress+"</span></li>"+
	            "<li><label class='labelClientTitle'>联系电话：</label><span class='spanText'>"+clientObj.tel+"</span></li></ul>");
	        clientMarker.addEventListener("mouseover", function(){this.openInfoWindow(infoWindow);});
	        clientMarker.addEventListener("mouseout", function(){this.closeInfoWindow();});
	        //将覆盖物加到hash表中
	        //clientMarkers[clientObj.id]=clientMarker;
	        /*if(clientMarkers[clientHasId]){
	        	//如果最后一个id都加载了，那么关闭加载数据提示
	        	$("#loadingMsg").window("close");
	        }*/
		}else{
			//地址解析实例
			var myGeo = new BMap.Geocoder();
		    // 将地址解析结果显示在地图上,并调整地图视野
		    myGeo.getPoint(clientObj.customerAddress, function(point){
		        if(point){
		            var clientMarker=new BMap.Marker(point,{icon:myIcon});
		            mapClass.ownMap.addOverlay(clientMarker);
		            //创建信息窗口
		            var infoWindow = new BMap.InfoWindow("<ul style='padding:0px'><li><label class='labelClientTitle'>客户名称：</label ><span class='spanText'>"+clientObj.customerName+"</span></li><li><label class='labelClientTitle'>客户地址：</label><span class='spanText'>"+clientObj.customerAddress+"</span></li>"+
		                "<li><label class='labelClientTitle'>联系电话：</label><span class='spanText'>"+clientObj.tel+"</span></li></ul>");
		            clientMarker.addEventListener("mouseover", function(){this.openInfoWindow(infoWindow);});
		            clientMarker.addEventListener("mouseout", function(){this.closeInfoWindow();});
		            //将覆盖物加到hash表中
		            //clientMarkers[clientObj.id]=clientMarker;
		           /* if(clientMarkers[clientHasId]){
		            	//如果最后一个id都加载了，那么关闭加载数据提示
		            	$("#loadingMsg").window("close");
		            }*/
		        }else{
		            //记录下没有解析出来的地址,clientMarkerError在clientMgr.js中声明
		            clientMarkerError[clientObj.id]=clientObj.customerAddress;
		            /*if(clientMarkerError[clientHasId]){
		            	//如果最后一个id都加载了，那么关闭加载数据提示
		            	$("#loadingMsg").window("close");
		            }*/
		        }
		    },city);
		}
};

/*==================================================添加行政区域----------------------------*/
/*mapClass.addBoundary=function(address){
	 var bdary = new BMap.Boundary();
	 if(cityBoundary.length!=0){
		 var len=cityBoundary.length;
		 for(var i=0;i<len;i++){
			 mapClass.ownMap.removeOverlay(cityBoundary[i]);
		 }
		 cityBoundary=[];//删除所有的行政区域覆盖物
	 }
	 if(address!="湖南省"){
		 bdary.get(address, function(rs){       //获取行政区域
	         //ownMap.clearOverlays();        //清除地图覆盖物
	         var count = rs.boundaries.length; //行政区域的点有多少个
	         for(var i = 0; i < count; i++){
	             var ply = new BMap.Polygon(rs.boundaries[i], {strokeWeight: 2, strokeColor: "#ff0000"}); //建立多边形覆盖物
	             mapClass.ownMap.addOverlay(ply);  //添加覆盖物
	             mapClass.ownMap.setViewport(ply.getPath());    //调整视野
	             cityBoundary.push(ply);
	         }
	     });
	 }else{
		 mapClass.ownMap.centerAndZoom("湖南省",9);
	 }
};*/

/*========================================绘制折线覆盖物-----------------------------*/
mapClass.addPloy=function(listPoint){
	 var points=[];
	 for(var i=0,len=listPoint.length;i<len;i++){
		points.push(new BMap.Point(listPoint[i].longitude,listPoint[i].latitude));
	}
	var polyline = new BMap.Polyline(points, {strokeColor:"red", strokeWeight:6, strokeOpacity:0.5});
	mapClass.ownMap.addOverlay(polyline);
	mapClass.ownMap.setViewport(polyline.getPath());//调整视野
};

/*====================================================员工覆盖物,自定义覆盖物------------------------*/
mapClass.workerMarker=function(point,text,hoverText){
    this._point = point;
    //this._id=id;
    this._text=text;
    this._hoverText=hoverText;
};
//继承
mapClass.workerMarker.prototype = new BMap.Overlay();
//初始化函数
mapClass.workerMarker.prototype.initialize = function(map){
    // 保存map对象实例
    this._map = map;
    // 创建div元素，作为自定义覆盖物的容器
    var div = document.createElement("div");
    div.style.position = "absolute";
    //div.style.zIndex = BMap.Overlay.getZIndex(this._point.lat);
    // 可以根据参数设置元素外观
    div.style.backgroundColor = "#EE5D5B";
    div.style.border = "1px solid #BC3B3A";
    //div.id=this._id;//保存下id，用来显示和隐藏
    div.style.color = "white";
    div.style.height = "18px";
    div.style.padding = "2px";
    div.style.lineHeight = "18px";
    div.style.whiteSpace = "nowrap";
    div.style.MozUserSelect = "none";
    div.style.fontSize = "12px";
    div.style.cursor="pointer";
     //将对象保存起来
    var that=this;

    //添加文字span
    var span = document.createElement("span");
    //span.className=this._id;
    div.appendChild(span);
    span.appendChild(document.createTextNode(this._text));

    //设置下面的指示箭头
    var arrow =this._arrow= document.createElement("div");
    arrow.style.background = "url("+header+"/static/images/workerLabel.png) no-repeat";
    arrow.style.position = "absolute";
    arrow.style.width = "11px";
    arrow.style.height = "10px";
    arrow.style.top = "22px";
    arrow.style.left = "10px";
    arrow.style.overflow = "hidden";
    div.appendChild(arrow);

    //绑定鼠标移动事件
    div.onmouseover = function(){
        this.style.backgroundColor = "#6BADCA";
        this.style.borderColor = "#0000ff";
        this.getElementsByTagName("span")[0].innerHTML = that._hoverText;
        arrow.style.backgroundPosition = "0px -20px";
    };
    div.onmouseout = function(){
        this.style.backgroundColor = "#EE5D5B";
        this.style.borderColor = "#BC3B3A";
        this.getElementsByTagName("span")[0].innerHTML = that._text;
        arrow.style.backgroundPosition = "0px 0px";
    };

    // 将div添加到覆盖物容器中
    mapClass.ownMap.getPanes().markerPane.appendChild(div);
    // 保存div实例
    this._div = div;
    // 需要将div元素作为方法的返回值，当调用该覆盖物的show、
    // hide方法，或者对覆盖物进行移除时，API都将操作此元素。
    return div;
};
//绘制覆盖物
mapClass.workerMarker.prototype.draw = function(){
    // 根据地理坐标转换为像素坐标，并设置给容器
    var position = this._map.pointToOverlayPixel(this._point);
    this._div.style.left = position.x - parseInt(this._arrow.style.left) + "px";
    this._div.style.top = position.y  - 30 + "px";
};


/*================================================最优路劲获取---------------------------------*/
/*
 *workerObj 是工作人员的对象，cursumeObj是客户的对象，adminAddress是管理员区域，获取路线的时候要用到,这里只封装单个方法，逻辑处理在每个js内部实现
 */
//驾车方案
mapClass.getRoute=function(workerObj,customerAddr,adminAddress,customerLng,customerLat){
    //搜索
    var transitGet = new BMap.DrivingRoute(adminAddress, {renderOptions: {map: mapClass.ownMap},
        policy: "BMAP_DRIVING_POLICY_LEAST_TIME",
        onSearchComplete:function (results){
            if (transitGet.getStatus() != BMAP_STATUS_SUCCESS){
                //如果没有获取成功，保存到错误结果中,直接返回
                   //routeError.push(workerObj.fullname);
	            	$.messager.alert("警告","客户地址不详细，无法获取路劲");
	            	$("#loadingMsg").window("close");
                   return ;
            }
            //由于上线的if中直接return了，这里不需要使用else
            var route={};
            //获取时间
            route.time=results.getPlan(0).getDuration();
            route.name=workerObj.fieldWorkerName;
            route.orderNumber=workerObj.pendingWorkOrderSize;
            // 获取方案的驾车线路
            var routeDetail=results.getPlan(0).getRoute(0);
            var s = [];
            for (var i = 0; i < routeDetail.getNumSteps(); i ++){
                var step = routeDetail.getStep(i);
                s.push((i + 1) + ":" + step.getDescription(false));
            }
           //保存驾车路线的详细描述
            route.lineDetail=s.join("\r\n");
            //将线路对象保存
            routeResults.push(route);
        },
        onMarkersSet: function(pois){
            //删除起点图标
            var start = pois[0].marker;
            mapClass.ownMap.removeOverlay(start);
        },
        onPolylinesSet: function(routes){
            //改变折线的颜色，并保存
            var ploy=routes[0].getPolyline();
            ploy.setStrokeColor("red");
            routeLines.push(ploy);
            
            //将所有的线路图描绘到地图上,需要都获取完路径
            if(routeLines.length!=0&&routeResults.length==fieldWorkerInfoList.length){
                $.each(routeLines,function(index,l){
                    mapClass.ownMap.addOverlay(l);
                });
                
               //将结果排序，显示出来                
               //将结果显示到表格中，排序取前5
                routeResults.sort(function(a,b){
                    return a.time.substring(0,a.time.length-2)-b.time.substring(0,b.time.length-2);
                });
                if(routeResults.length>5){
                    //截取前十条
                    routeResults=routeResults.slice(0,5);
                }
                //用addRow方法加载到表格中
                $.each(routeResults,function(index,r){
                    $('#showResultsTable').datagrid('appendRow',{
                        name: r.name,
                        orderNumber:r.orderNumber,
                        time:r.time,
                        line: r.lineDetail,
                        opt:"opt"
                    });
                });
                $("#showResultsPanel").window("open");
                $("#loadingMsg").window("close");
            }
        }
    });
    //对每一个外服人员进行路径获取
    if(customerLat){
    	transitGet.search(new BMap.Point(workerObj.latestLongitude,workerObj.latestLatitude),new BMap.Point(parseFloat(customerLng),parseFloat(customerLat)));
    }else{
    	transitGet.search(new BMap.Point(workerObj.latestLongitude,workerObj.latestLatitude),customerAddr);
    }
};
//公交方案
mapClass.getRouteByBus=function(workerObj,customerAddr,adminAddress,customerLng,customerLat){
    //搜索
    var transitGet = new BMap.TransitRoute(adminAddress, {renderOptions: {map: mapClass.ownMap},
        policy: "BMAP_TRANSIT_POLICY_LEAST_TIME",
        onSearchComplete:function (results){
            if (transitGet.getStatus() != BMAP_STATUS_SUCCESS){
                //如果没有获取成功，保存到错误结果中,直接返回
                   //routeError.push(workerObj.fullname);
            	routeError++;//错误的路径数据加1
            	//如果所有的人都没有获取到路径，则显示给用户检索到0条路径
            	if(routeError==fieldWorkerInfoList.length){
            		$.messager.alert("警告","检索到0条路径");
            		//添加客户标签
            		 var myIcon = new BMap.Icon("http://api.map.baidu.com/img/dest_markers.png", new BMap.Size(39,32),{imageOffset:new BMap.Size(0,-30)});
                     var clientMarker=new BMap.Marker(new BMap.Point(customerLng,customerLat),{icon:myIcon});
         	         mapClass.ownMap.addOverlay(clientMarker);
	            	$("#loadingMsg").window("close");
            	}	            	
                   return ;
            }
            //由于上线的if中直接return了，这里不需要使用else
            var route={};
            //获取时间
            route.time=results.getPlan(0).getDuration();
            route.name=workerObj.fieldWorkerName;
            route.orderNumber=workerObj.pendingWorkOrderSize;
            // 获取方案的驾车线路
            var routeDetail=results.getPlan(0).getDescription(false);
            /*var routeDetail=results.getPlan(0).getRoute(0);
            var s = [];
            for (var i = 0; i < routeDetail.getNumSteps(); i ++){
                var step = routeDetail.getStep(i);
                s.push((i + 1) + ":" + step.getDescription());
            }*/
           //保存驾车路线的详细描述
            //route.lineDetail=s.join("\r\n");
            route.lineDetail=routeDetail;
            //将线路对象保存
            routeResults.push(route);
        },
        onMarkersSet: function(pois){
            //删除起点图标
            var start = pois[0].marker;
            mapClass.ownMap.removeOverlay(start);
        },
        onPolylinesSet: function(routes){
            //改变折线的颜色，并保存
        	for(var i=0;i<routes.length;i++){
        		var ploy=routes[i].getPolyline();
                ploy.setStrokeColor("red");
                routeLines.push(ploy);
        	}
            
            //将所有的线路图描绘到地图上,需要都获取完路径（存在路径并且后去的路径数和为获取的数量加起来为员工数量就代表路径获取完毕，应该显示表格）
            if(routeLines.length!=0&&routeError+routeResults.length==fieldWorkerInfoList.length){
                $.each(routeLines,function(index,l){
                    mapClass.ownMap.addOverlay(l);
                });
              //将结果排序，显示出来
                
                //将结果显示到表格中，排序取前5
             routeResults.sort(function(a,b){
                 return a.time.substring(0,a.time.length-2)-b.time.substring(0,b.time.length-2);
             });
             if(routeResults.length>5){
                 //截取前十条
                 routeResults=routeResults.slice(0,5);
             }
             //用addRow方法加载到表格中
             $.each(routeResults,function(index,r){
                 $('#showResultsTable').datagrid('appendRow',{
                     name: r.name,
                     orderNumber:r.orderNumber,
                     time:r.time,
                     line: r.lineDetail,
                     opt:"opt"
                 });
             });
             $("#showResultsPanel").window("open");
             $("#loadingMsg").window("close");
            }               
        }
    });
    //对每一个外服人员进行路径获取
    if(customerLat){
    	//transitGet.search(new BMap.Point(workerObj.latestLongitude,workerObj.latestLatitude),new BMap.Point(parseFloat(customerLng),parseFloat(customerLat)));
    	transitGet.search(new BMap.Point(workerObj.latestLongitude,workerObj.latestLatitude),new BMap.Point(customerLng,customerLat));
    }else{
    	transitGet.search(new BMap.Point(workerObj.latestLongitude,workerObj.latestLatitude),customerAddr);
    }
};