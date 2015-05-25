function popup(url, width, height, withToolbars)
{
    var url      = url;
    var width    = width;
    var height   = height;
    var left     = (screen.width  - width)  / 2;
    var top      = (screen.height - height) / 2;

    if(withToolbars)
        property = 'left='+left+', top='+top+', toolbar=1,scrollbars=1,location=1,statusbar=1,menubar=1,resizable=1,alwaysRaised,width='+width+',height='+height;
    else
        property = 'left='+left+', top='+top+', toolbar=0,scrollbars=0,location=0,statusbar=0,menubar=0,resizable=1,alwaysRaised,width='+width+',height='+height;
        
    window.open(url, "_blank", property);
}

