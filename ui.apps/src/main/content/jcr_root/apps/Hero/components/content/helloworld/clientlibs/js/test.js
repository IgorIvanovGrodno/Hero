(function ($, Granite){
    Granite.I18n.setLocale("en");
    const someLambda = () => {
        return () => {
            if (window.location.href.indexOf("fr") >= 0) {
                Granite.I18n.setLocale("fr");
            }
            return Granite.I18n.get("Hello world! {0}", ['Ihar']);
        };
    };
    const myVar = ban();
    $("p").css('color', 'green');
    alert(myVar + someLambda()());
})(jQuery, Granite)