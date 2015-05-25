function bodyWidth(){ return document.body.offsetWidth || window.innerWidth || document.documentElement.clientWidth || 0; };
function bodyHeight(){ return document.body.offsetHeight || window.innerHeight || document.documentElement.clientHeight || 0; };

var currentPkey=null;
function showEditTagDialog(pkey)
{
  currentPkey = pkey;
  if(createFieldDialog==null)
     createFieldDialog = new Dialog.Box("dialog_tagging");
     
  new Ajax.Request("rpc/getTag.jsp",
  {
    parameters:
    {
      pkey: currentPkey
    },
    onSuccess: function (transport)
    {
       $("tag").value= transport.responseText.trim();
       createFieldDialog.show();
       $("tag").focus();
    }
  });
}



(function() {
  Event.observe(window, "load", function() 
  {
      Event.observe("tag","keydown",function(event)
      {
        if(event.keyCode === 13)
        {
            event.stop();
            $("button_close_tagging").click();
        }
      }); 
      
      $("button_close_tagging").observe("click", function()
      {
        var myAjax = new Ajax.Request("rpc/saveTag.jsp",
        {
          parameters:
          {
            pkey: currentPkey,
            tag: $F("tag")
          },
          onSuccess: function (transport)
          {
            createFieldDialog.hide();
          }
        });
      });
  });
})();

