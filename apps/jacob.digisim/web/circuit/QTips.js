/**
 * You can call them directly on an element - example:
 * 
 * Code:
 * 
 * Ext.get('some_element_id').addQuickTip('QuickTip', 'QuickTip Title');	// QuickTip Title is optional
 * Ext.get('some_element_id').removeQuickTip();
 * 
 * ... or you can add/remove QuickTips to many elements at once by passing an array, like so:
 * 
 * Code:
 * 
 * // To Add (title is optional)
 * Ext.Element.addQuickTips([
 * 	{id: 'element1_id', tip: 'El1 Tip', title: 'El1 Title'},
 * 	{id: 'element2_id', tip: 'El2 Tip', title: 'El2 Title'},
 * 	{id: 'element3_id', tip: 'El3 Tip'},
 * 	{id: 'element4_id', tip: 'El4 Tip'}
 * ]);
 * 
 * // To Remove
 * Ext.Element.removeQuickTips(['element1_id','element2_id','element3_id','element4_id']);
 * 
 * That's it, pretty simple.
 * 
 **/
var QTips = function() {
	return {
		addQuickTip : function(tip, title) {
			Ext.Element.addQuickTips([{id: this.id, tip: tip, title: title}]);
			return this;
		},

		addQuickTips : function(obj) {
			if (typeof obj == 'object') {
				for (i = 0; i < obj.length; i++) {
					var o = obj[i];
					var el = Ext.get(o.id).dom;
	
					el.setAttribute('ext:qtip', o.tip);
					el.setAttribute('ext:width', "90");
					if (o.title) {
						el.setAttribute('ext:qtitle', o.title);
					}
				}
			}
		},

		removeQuickTip : function() {
			Ext.Element.removeQuickTips([this.id]);
			return this;
		},

		removeQuickTips : function(obj) {
			if (typeof obj == 'object') {
				for (i = 0; i < obj.length; i++) {
					var el = Ext.get(obj[i]).dom;

					el.removeAttribute('ext:qtip');
					el.removeAttribute('ext:qtitle');
				}
			}
		}
	};
}();

Ext.apply(Ext.Element.prototype, {addQuickTip: QTips.addQuickTip, removeQuickTip: QTips.removeQuickTip});
Ext.Element.addQuickTips = QTips.addQuickTips;
Ext.Element.removeQuickTips = QTips.removeQuickTips;
