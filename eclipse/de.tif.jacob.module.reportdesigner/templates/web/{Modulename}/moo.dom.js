//moo.dom by Valerio Proietti (http://mad4milk.net) MIT Open Source license;
//v 1.5 (beta);

function $S() {
	var elements = [];
	$c(arguments).each(function(sel){
		if (typeof sel == 'string') {
			sel.getElements().each(function(el){
				elements.push(el);
			});
		}
		else elements.push(sel);
	});
	return elements;
}

/*------------------String Prototypes----------------------*/

Object.extend(String.prototype, {
	getElements: function(filter){
		var params = [];
		this.split(' ').each(function(arg, j){
			params[j] = param = [];
			if (arg.indexOf('#') > -1) {
				var bits = arg.split('#');
				param['tag'] = bits[0] || '*';
				param['id'] = bits[1];
			}
			else if (arg.indexOf('.') > -1) {
				var bits = arg.split('.');
				param['tag'] = bits[0] || '*';
				param['class'] = bits[1];
			}
			else param['tag'] = arg;
		});
		var filter = filter || document;
		filter = $c(filter.getElementsByTagName('*'));
		params.each(function(param, k){
			if (param['tag'] != '*' && k == 0) filter = filter.filterByTagName(param['tag']);
			else if (k != 0) filter = filter.getElementsByTagName(param['tag']);
			if (param['id']) filter = filter.filterById(param['id']);
			if (param['class']) filter = filter.filterByClassName(param['class']);
		});
		return filter;
	},
	
	getElementsBySelector: function(filter){
		if (!filter) filter = null;
		var elements = [];
		this.split(',').each(function(selector){
			elmnts = selector.replace(/^\s*|\s*$/g,"").getElements(filter);
			elmnts.each(function(el){
				elements.push(el);
			});
		});
		return elements;
	},

	rgbToHex: function(array){
		var rgb = this.match(new RegExp('([\\d]{1,3})', 'g'));
		if (rgb[3] == 0) return 'transparent';
		var hex = [];
		for (var i = 0; i < 3; i++){
			var bit = (rgb[i]-0).toString(16);
			hex.push(bit.length == 1 ? '0'+bit : bit);
		}
		var hexText = '#'+hex.join('');
		if (array) return hex;
		else return hexText;
	},

	hexToRgb: function(array){
		var hex = this.match(new RegExp('^[#]{0,1}([\\w]{1,2})([\\w]{1,2})([\\w]{1,2})$'));
		var rgb = [];
		for (var i = 1; i < hex.length; i++){
			if (hex[i].length == 1) hex[i] += hex[i];
			rgb.push(parseInt(hex[i], 16));
		}
		var rgbText = 'rgb('+rgb.join(',')+')';
		if (array) return rgb;
		else return rgbText;
	}	
});


/*----------------------Array Prototypes-----------------------*/

function $c(array){
	var nArray = [];
	for (i=0;el=array[i];i++) nArray.push(el);
	return nArray;
}

Object.extend(Array.prototype, {
	iterate: function(func){
		for(var i=0;ob=this[i];i++) func(ob, i);
	},

	action: function(actions){
		this.each(function(el){
			if (actions.initialize) actions.initialize.apply(el);
			for(action in actions){
				if (action.slice(0,2) == 'on') el[action] = actions[action];
			}
		});
	},
	
	filterById: function(id){
		var found = [];
		this.each(function(el){
			if (el.id == id) found.push(el);
		});
		return found;
	},

	filterByClassName: function(className){
		var found = [];
		this.each(function(el){
			if (Element.hasClassName(el, className)) found.push(el);
		});
		return found;
	},

	filterByTagName: function(tagName){
		var found = [];
		this.each(function(el){
			if (el.tagName.toLowerCase() == tagName) found.push(el);
		});
		return found;
	},

	getElementsByTagName: function(tagName){
		var found = [];
		this.each(function(el){
			$c(el.getElementsByTagName(tagName)).each(function(tn){
				found.push(tn);
			});
		});
		return found;
	}
});

if(!Array.prototype.each) Array.prototype.each = Array.prototype.iterate;