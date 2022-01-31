(function ($){
    const someLambda = () => {
        return () => {return ' test_lambda'};
    };
    const myVar = ban();
    $("p").css('color', 'green');
    alert(myVar + someLambda()());
})(jQuery)