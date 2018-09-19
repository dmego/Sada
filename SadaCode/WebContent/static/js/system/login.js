$(document).ready(function () {
	//去 注册
	$("#toRegist").click(function () {
		$("#login_container").hide(500);
		$("#regist_container").show(500);
	});
	//去 登录
	$("#toLogin").click(function () {
		$("#regist_container").hide(500);
		$("#login_container").show(500);
	});
});

//开发过程中不使用验证码，注释下面两行代码
var hadajax2 = false;
var hadajax1 = false;

//*************开发过程中代码************* */
// var hadajax2 = true;
// var hadajax1 = true;
// $("#submit1").click(function (e) {
// 	$("#loginForm").attr("action", "menuAction_login.do");
// 	$("#loginForm").submit();
// });

/************************************ */
/**
 * 登录页面的极验验证
 */
var handler1 = function (captchaObj) {
	$("#submit1").click(function (e) {
		var result = captchaObj.getValidate();
		if (!result) {
			$("#notice1").show();
			$("#msg1").empty().append("请先完成验证");
			setTimeout(function () {
				$("#notice1").hide();
			}, 2000);
			return false;
		} else {
			$.ajax({
				url: 'gtValidate',
				type: 'POST',
				dataType: 'json',
				data: {
					geetest_challenge: result.geetest_challenge,
					geetest_validate: result.geetest_validate,
					geetest_seccode: result.geetest_seccode
				},
				success: function (data) {
					if (data.status === 'success') {
						console.log('验证成功');
					} else if (data.status === 'fail') {
						console.log('验证失败');
					}
					// //数据校验
					// $("#loginForm").bootstrapValidator({
					// 	message: '请输入有效值',
					// 	feedbackIcons: {
					// 		valid: 'glyphicon glyphicon-ok',
					// 		invalid: 'glyphicon glyphicon-remove',
					// 		validating: 'glyphicon glyphicon-refresh'
					// 	},
					// 	fields: {
					// 		registname: {
					// 			validators: {
					// 				notEmpty: {
					// 					message: '请输入用户名'
					// 				},
					// 				stringLength: {
					// 					min: 3,
					// 					max: 20,
					// 					message: '用户名长度必须在3到20之间'
					// 				},
					// 				regexp: {
					// 					regexp: /^[a-zA-Z0-9_\.]+$/,
					// 					message: '用户名格式不正确'
					// 				}
					// 			}
					// 		},
					// 		registpass1: {
					// 			message: '密码无效',
					// 			validators: {
					// 				notEmpty: {
					// 					message: '密码不能为空'
					// 				},
					// 				stringLength: {
					// 					min: 3,
					// 					max: 10,
					// 					message: '密码长度必须在3到10之间'
					// 				},
					// 				identical: { //相同
					// 					field: 'registpass2', //需要进行比较的input name值
					// 					message: '两次密码不一致'
					// 				},
					// 				regexp: {
					// 					regexp: /^[a-zA-Z0-9_\.]+$/,
					// 					message: '密码格式不正确'
					// 				}
					// 			}
					// 		}
					// 	}
					// });

					$("#loginForm").attr("action", "menuAction_login.do");
					$("#loginForm").submit();

				}
			});

		}
		e.preventDefault();
	});
	// 将验证码加到id为captcha的元素里，同时会有三个input的值用于表单提交
	captchaObj.appendTo("#captcha1");
	captchaObj.onReady(function () {
		$("#wait1").hide();
	});
};

if (hadajax1 == false) {
	$.ajax({
		url: "gtRegister?t=" + (new Date()).getTime(), // 加随机数防止缓存
		type: "get",
		dataType: "json",
		success: function (data) {
			hadajax1 = true;
			// 调用 initGeetest 初始化参数
			// 参数1：配置参数
			// 参数2：回调，回调的第一个参数验证码对象，之后可以使用它调用相应的接口
			initGeetest({
				gt: data.gt,
				challenge: data.challenge,
				new_captcha: data.new_captcha, // 用于宕机时表示是新验证码的宕机
				offline: !data.success, // 表示用户后台检测极验服务器是否宕机，一般不需要关注
				product: "popup", // 产品形式，包括：float，popup
				width: "100%"
			}, handler1);
		}
	});
}


/**
 * 注册页面的极验验证
 */
var handler2 = function (captchaObj) {
	$("#submit2").click(function (e) {
		var result = captchaObj.getValidate();
		if (!result) {
			$("#notice2").show();
			$("#msg2").empty().append("请先完成验证");
			setTimeout(function () {
				$("#notice2").hide();
			}, 2000);
		} else {
			$.ajax({
				url: 'gtValidate',
				type: 'POST',
				dataType: 'json',
				data: {
					geetest_challenge: result.geetest_challenge,
					geetest_validate: result.geetest_validate,
					geetest_seccode: result.geetest_seccode
				},
				success: function (data) {
					if (data.status === 'success') {
						console.log('验证成功');
					} else if (data.status === 'fail') {
						console.log('验证失败');
					}
					// $("#submit2").click(function (e) {
					// 	//数据校验
					// 	$("#registForm").bootstrapValidator({
					// 		message: '请输入有效值',
					// 		feedbackIcons: {
					// 			valid: 'glyphicon glyphicon-ok',
					// 			invalid: 'glyphicon glyphicon-remove',
					// 			validating: 'glyphicon glyphicon-refresh'
					// 		},
					// 		fields: {
					// 			registname: {
					// 				validators: {
					// 					notEmpty: {
					// 						message: '请输入用户名'
					// 					},
					// 					stringLength: {
					// 						min: 3,
					// 						max: 20,
					// 						message: '用户名长度必须在3到20之间'
					// 					},
					// 					regexp: {
					// 						regexp: /^[a-zA-Z0-9_\.]+$/,
					// 						message: '用户名格式不正确'
					// 					}
					// 					// remote: {
					// 					// 	url: basePath + "/userAction_checkRegistName.do", //验证的服务器地址
					// 					// 	//自定义提交数据，默认值提交当前input value
					// 					// 	data: function (validator) {
					// 					// 		return {
					// 					// 			userName: $('#registname').val()
					// 					// 		};
					// 					// 	},
					// 					// 	message: '该用户名已被占用', //提示消息
					// 					// 	//每输入一个字符，就发ajax请求，服务器压力还是太大，
					// 					// 	//设置2秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
					// 					// 	delay: 2000,
					// 					// 	type: 'GET' //请求方式
					// 					// }
					// 				}
					// 			},
					// 			registpass1: {
					// 				message: '密码无效',
					// 				validators: {
					// 					notEmpty: {
					// 						message: '密码不能为空'
					// 					},
					// 					stringLength: {
					// 						min: 3,
					// 						max: 10,
					// 						message: '密码长度必须在3到10之间'
					// 					},
					// 					identical: { //相同
					// 						field: 'registpass2', //需要进行比较的input name值
					// 						message: '两次密码不一致'
					// 					},
					// 					regexp: {
					// 						regexp: /^[a-zA-Z0-9_\.]+$/,
					// 						message: '密码格式不正确'
					// 					}
					// 				}
					// 			},
					// 			registpass2: {
					// 				message: '确认密码无效',
					// 				validators: {
					// 					notEmpty: {
					// 						message: '确认密码不能为空'
					// 					},
					// 					stringLength: {
					// 						min: 3,
					// 						max: 10,
					// 						message: '确认密码长度必须在3到10之间'
					// 					},
					// 					identical: { //相同
					// 						field: 'registpass1', //需要进行比较的input name值
					// 						message: '两次密码不一致'
					// 					},
					// 					regexp: {
					// 						regexp: /^[a-zA-Z0-9_\.]+$/,
					// 						message: '密码格式不正确'
					// 					}
					// 				}
					// 			}
					// 		}
					// 	});
						$("#registForm").attr("action", "menuAction_regist.do");
						$("#registForm").submit();
					// });
				}
			})
		}
		e.preventDefault();
	});
	// 将验证码加到id为captcha的元素里，同时会有三个input的值用于表单提交
	captchaObj.appendTo("#captcha2");
	captchaObj.onReady(function () {
		$("#wait2").hide();
	});

};


function geeRegist() {
	if (hadajax2 == false) {
		$.ajax({
			url: "gtRegister?t=" + (new Date()).getTime(), // 加随机数防止缓存
			type: "get",
			dataType: "json",
			success: function (data) {
				hadajax2 = true;
				// 调用 initGeetest 初始化参数
				// 参数1：配置参数
				// 参数2：回调，回调的第一个参数验证码对象，之后可以使用它调用相应的接口
				initGeetest({
					gt: data.gt,
					challenge: data.challenge,
					new_captcha: data.new_captcha, // 用于宕机时表示是新验证码的宕机
					offline: !data.success, // 表示用户后台检测极验服务器是否宕机，一般不需要关注
					product: "popup", // 产品形式，包括：float，popup
					width: "100%"
					// 更多配置参数请参见：http://www.geetest.com/install/sections/idx-client-sdk.html#config
				}, handler2);
			}
		});
	}
}