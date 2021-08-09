/***************************
 *
 *	XMLOBJ	: XML Object
 *		Const:
 *			Stop
 *			Init
 *			Updating
 *			Ready
 *			Fail
 *		Properties:
 *			status			// XML Status (Stop, Init, Updating, Ready, Fail, Error)
 *			times				// XML download times
 *		Methods:
 *			Status();			// Status (return String)
 *			setLink(XMLLink);	// change XML Link
 *			init();
 *			download();			// load the XML
 *			getRoot();			// Reture the XML Root node
 *
 *	Other Function:
 *		xmlGetNode(XMLNode, XPATH);				// Get Node
 *		xmlGet(XMLNode, XPATH, DefaultValue);	// Get Value
 *		xmlGetLength(XMLNode, XPATH);				// Get Nodes length
 *
 */
function XMLOBJ(l) {

//	Setting:
	this.Async = true;

//	Data:
	var i = 0;
	this.Stop	= ++i;
	this.Init	= ++i;
	this.Updating	= ++i;
	this.Ready	= ++i;
	this.Fail	= ++i;
	this.Error	= ++i;

	this.METHOD1 = ++i;
	this.METHOD2 = ++i;
	this.METHOD3 = ++i;
	this.LIB = '-';

	this.Method = this.METHOD1;

	this.StatusString = new Array();
	this.StatusString[this.Stop]	= 'Stop';
	this.StatusString[this.Init]	= 'Init';
	this.StatusString[this.Updating]	= 'Updating';
	this.StatusString[this.Ready]	= 'Ready';
	this.StatusString[this.Fail]	= 'Fail';
	this.StatusString[this.Error]	= 'Error';

	this.link = l;
	this.data;
	this.datalast;
	this.datatmp;
	this.status = this.Stop;
	this.times;
	this.last;

// Method:
	this.Status = function() {
		var tmp;

		try {
			tmp = this.StatusString[this.status];
		} catch (e) {
			tmp = this.StatusString[this.Error];
		}

		return tmp;
	}	// Status

	this.setLink = function(l) {
		this.link = l;
	}	// setLink

	this.checkState = function() {
		if (this.datatmp.readyState == 4) {
			if (this.Method != this.METHOD3 || this.datatmp.parseError.errorCode == 0) {
				this.datalast = null;
				this.datalast = this.data;
				if (this.Method != this.METHOD3) {
//					this.data = this.datatmp.responseXML.cloneNode(true);
					this.data = this.datatmp.responseXML;	//.cloneNode(true);
				} else {
					this.data = this.datatmp.cloneNode(true);
				}
				if (true || this.Method != this.METHOD1) {
					try {
						this.data.setProperty("SelectionLanguage","XPath");	// set IE XPath xxx[1] is first node
					} catch (e) {
					}
				}
				this.status = this.Ready;
			} else {
				this.status = this.Fail;
window.status=(this.datatmp.parseError.errorCode + ' - ' + this.datatmp.parseError.reason);
			}
		} else if (this.datatmp.readyState > 4) {
			this.status = this.Fail;
		} else {
			var _self = this;
			setTimeout(function(){
				_self.checkState();
				}, 1 * 50);
		}
	}

	this.init = function() {
//		var ARR_ACTIVEX = ["MSXML4.DOMDocument", "MSXML3.DOMDocument", "MSXML2.DOMDocument", "MSXML.DOMDocument", "Microsoft.XmlDom"];
//		var ARR_ACTIVEX = ["Microsoft.XmlDom", "Microsoft.XMLHTTP"];
		var ARR_ACTIVEX = ["Microsoft.XMLHTTP"];
		var ARR_ACTIVEX2 = ["MSXML4.DOMDocument", "MSXML3.DOMDocument", "MSXML2.DOMDocument", "MSXML.DOMDocument", "Microsoft.XmlDom"];
		var i;
		var tmp;

		try {
			tmp = true;

			if (window.XMLHttpRequest) {
				try {
					this.datatmp = new window.XMLHttpRequest();
					this.LIB = 'window.XMLHttpRequest()';
					this.Method = this.METHOD1;
					tmp = false;
				} catch (e) {
				}
			} else if (window.ActiveXObject) {
				for (i=0; tmp && i<ARR_ACTIVEX.length; i++) {
					try {
						this.datatmp = new ActiveXObject(ARR_ACTIVEX[i]);
						this.LIB = ARR_ACTIVEX[i];
						tmp = false;
						this.Method = this.METHOD2;
						this.datatmp.setProperty("SelectionLanguage","XPath");
					} catch (e) {
					}
				}
				for (i=0; tmp && i<ARR_ACTIVEX2.length; i++) {
					try {
						this.datatmp = new ActiveXObject(ARR_ACTIVEX2[i]);
						this.LIB = ARR_ACTIVEX2[i];
						tmp = false;
						this.Method = this.METHOD3;
						this.datatmp.async = this.Async;
						this.datatmp.setProperty("SelectionLanguage","XPath");
					} catch (e) {
					}
				}
			} else {
				this.status = this.Fail;
			}

			if (tmp) {
				this.status = this.Fail;
				window.status = 'Init - Fail';
			} else {
/*
				var _self = this;
				this.datatmp.onreadystatechange = function() {
					_self.checkState;
				}
*/
				this.status = this.Init;
			}
			this.times = 0;
		} catch (e) {
			this.status = this.Fail;
			window.status = 'Init - ' + e;
		}
	}	// init

	this.Reload = function() {
		this.status = this.Ready;
		alert('World	' + this.datatmp.readyState);
	}

	this.download = function() {
		var i = 0;
		try {
			if (this.status != this.Updating) {
				i = 10;
				this.times++;
/*
				if (this.status == this.Stop || this.status == this.Fail) {
					this.init();
				}
*/
				i = 30;
				this.status = this.Updating;
/*
this.datatmp.onreadystatechange = function() {
	alert('Hello World');
	if(this.datatmp.readyState == 4 && this.datatmp.status == 200) {
		alert(this.datatmp.responseText);
	}
}
*/
				if (this.Method != this.METHOD3) {
					this.datatmp.open("GET", this.link + (this.link.indexOf('?') > 0 ? '&' : '?') + 'nocache=' + (''+(new Date())).replace(/ /, '_'), this.Async);
					this.datatmp.send(null);
				} else {
					this.datatmp.load(this.link + (this.link.indexOf('?') > 0 ? '&' : '?') + 'nocache=' + (''+(new Date())).replace(/ /, '_'));
				}
				i = 50;
				if (false && this.data.readyState == 4) {
					i = 70;
					this.datalast = null;
					this.datalast = this.data;
					this.data = this.datatmp.cloneNode(true);
					this.status = this.Ready;
					i = 90;
				}

				var _self = this;
				setTimeout(function(){
					_self.checkState();
					}, 1 * 50);
			}
			i = 110;
		} catch (e) {
//			window.status = this.StatusString[this.status] + ' : ' + i + ' - ' + e;
			this.status = this.Fail;
		}
	}	// download

	this.getRoot = function(l) {
		var r = null;
		try {
			if (arguments.length > 0 && l == 1) {
//				if (this.Method != this.METHOD3) {
//					r = this.datalast.responseXML.documentElement;
//				} else {
					r = this.datalast;	//.documentElement;	//.documentElement;
//				}
			} else {
//				if (this.Method != this.METHOD3) {
//					r = this.data.responseXML.documentElement;
//				} else {
					r = this.data;	// .documentElement;
//				}
			}
		} catch (e) {
//window.status='By '+arguments.length+' = '+e;
//alert('By '+arguments.length+' = '+e);
			r = null;
		}
		return r;
	}

	this.GetNode = function(xpath) {
		var tmp, tmp2, tmp3;

		try {
			tmp3 = true;
			if (this.Method == this.METHOD1) {
				try {
					//xmlDoc.evaluate(xpath, xmlDoc, null, XPathResult.ANY_TYPE,null);
					tmp2 = this.data.evaluate(xpath, this.data, null, XPathResult.ANY_TYPE, null);
					tmp = tmp2.iterateNext();
					tmp3 = false;
				} catch (e) {
				}
			}
			if (tmp3) {
				tmp = this.data.selectSingleNode(xpath);
			}
		} catch (e) {
//alert('GetNode: ' + e);
			tmp = null;
		}

		return tmp;
	}	// GetNode
	this.GetLength = function(xpath) {
		var tmp, tmp2, tmp3;

		try {
			tmp3 = false;
			if (this.Method == this.METHOD1) {
				try {
					tmp2 = this.data.evaluate(xpath, this.data, null, XPathResult.ANY_TYPE,null);
					tmp3 = true;
				} catch (e) {
				}
			}
			if (tmp3) {
				tmp = 0;
				while (tmp3 = tmp2.iterateNext()) tmp++;
			} else {
				tmp = this.data.selectNodes(xpath).length;
			}
		} catch (e) {
//alert('GetLength: ' + this.data + ' = ' + tmp2 + ' : ' + e);
			tmp = 0;
		}

		return tmp;
	}	// GeLengtht
	this.Get = function(xpath, defaultvalue) {
		var tmp;

		var n;
		var p;
		var i;
		var t;

		try {
			n = this.GetNode(xpath);
			if (n == null || !n) {
				i = xpath.lastIndexOf('/');
				if (i >= 0) {
					p = xpath.substring(0, i);
					t = xpath.substring(i+1, xpath.length);
				} else {
					p = '';
					t = xpath;
				}

/*
				i = p.indexOf('/');
				if (i == 0) {
					p = p.substring(1, p.length);
				}
*/

				if (p.length > 0) {
					n = this.GetNode(p);
/*
					if (n == null) {
						n = this.GetNode('/'+p);
					}
*/
				} else {
					n = this.data;
				}

				tmp = n.getAttribute(t);
				for (i=0; (!tmp || tmp == null) && i<n.childNodes.length; i++) {
					if (n.childNodes.item(i).tagName == t) {
						tmp = n.childNodes.item(i).childNodes.item(0).data;
					}
				}

				if (!tmp || tmp == null) {
					if (arguments.length >= 2) {
						tmp = defaultvalue;
					} else {
						tmp = null;
					}
				}
			} else {
try {
				tmp = n.childNodes.item(0).data;
} catch (e) {
				tmp = n.childNodes[0].nodeValue;
}
			}

		} catch (e) {
			if (arguments.length >= 2) {
				tmp = defaultvalue;
			} else {
				tmp = null;
			}
		}

		return tmp;
	}	// Get
}	// XMLOBJ

function xmlGetNode(node, xpath) {
	var tmp, tmp2;

	try {
		if (window.ActiveXObject) {
			tmp = node.selectSingleNode(xpath);
		} else {
			tmp2 = node.evaluate(xpath, node, null, XPathResult.ANY_TYPE, null);
			tmp = tmp2.iterateNext();
		}
	} catch (e) {
		tmp = null;
	}

	return tmp;
}	// xmlGetNode
function xmlGet(node, xpath, defaultvalue) {
	var tmp;

	var n;
	var p;
	var i;
	var t;

	try {
		n = xmlGetNode(node, xpath);
		if (n == null || !n) {
			i = xpath.lastIndexOf('/');
			if (i >= 0) {
				p = xpath.substring(0, i);
				t = xpath.substring(i+1, xpath.length);
			} else {
				p = '';
				t = xpath;
			}

			i = p.indexOf('/');
			if (i == 0) {
				p = p.substring(1, p.length);
			}

			if (p.length > 0) {
				n = xmlGetNode(node, p);
				if (n == null) {
					n = xmlGetNode(node, '/'+p);
				}
			} else {
				n = node;
			}

			tmp = n.getAttribute(t);
			for (i=0; (!tmp || tmp == null) && i<n.childNodes.length; i++) {
				if (n.childNodes.item(i).tagName == t) {
					tmp = n.childNodes.item(i).childNodes.item(0).data;
				}
			}

			if (!tmp || tmp == null) {
				if (arguments.length >= 3) {
					tmp = defaultvalue;
				} else {
					tmp = null;
				}
			}
		} else {
			try {
				tmp = n.childNodes.item(0).data;
			} catch (e) {
				tmp = n.childNodes[0].nodeValue;
			}
		}

	} catch (e) {
		if (arguments.length >= 3) {
			tmp = defaultvalue;
		} else {
			tmp = null;
		}
	}

	return tmp;
}	// xmlGet
function xmlGetLength(node, xpath) {
	var tmp, tmp2;

	try {
		if (window.ActiveXObject) {
			tmp = node.selectNodes(xpath).length;
		} else {
			tmp2 = node.evaluate(xpath, node, null, XPathResult.ANY_TYPE,null);
			tmp = 0;
			while (tmp2.iterateNext()) tmp++;
		}
	} catch (e) {
		tmp = 0;
	}

	return tmp;
}	// xmlGeLengtht

/////////////////////////////////////////////////////////////////////

function StringBuffer() {
	this.strings = new Array();
	this.length = this.strings.length;

	this.append = function(s) {
		this.strings.push(s);
		this.length = this.strings.length;
	}

	this.toString = function(s) {
		return this.strings.join(s);
	}

	this.removeAll = function() {
		while (this.strings.length > 0) {
			delete this.strings[this.strings.length-1];
			this.strings.length--;
		}
		this.length = this.strings.length;
	}
	return this;
}
