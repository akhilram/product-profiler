    (function($,W,D)
    {
        var JQUERY4U = {};

        JQUERY4U.UTIL =
        {
            setupFormValidation: function()
            {
            //form validation rules
            $("#search-form").validate({
                rules: {
                    keywords: "required",
                    minPrice: {
                        number: true,
                        min: 0
                        // greaterThan: "minPrice"
                    }
                },
                messages: {
                    keywords: "Please enter keywords",
                    minPrice: {
                        min: "Price cannot be negative",
                        number: "Price has to be a proper number"
                    }
                },
                submitHandler: function(form) {
                    form.submit();
                }
            });
}
}

    //when the dom has loaded setup form validation rules
    $(D).ready(function($) {
        window.resultTemplate = Handlebars.compile($("#entry-template").html());
        window.featureTemplate = Handlebars.compile($("#feature-template").html());
        Handlebars.registerHelper('ifTrue', function(v1, v2, options) {
            if(v1 == v2) {
                return options.fn(this);
            }
            return options.inverse(this);
        });
        
        JQUERY4U.UTIL.setupFormValidation();
        
        $(".paginate").click(getJSONData);

        

        // max price greater than min price
        $.validator.addMethod("greaterThan",
            function(value, element, param) {
                var i = parseFloat($("#minPrice").val());
                var j = parseFloat($("#maxPrice").val());
                if (isNaN(i)||isNaN(j)) {
                    return true;
                };
                var stat = (i<=j) ? true : false;
                console.log(stat);
                return stat;
            }
            );

        // $.ajaxSetup({ cache: true });
        // $.getScript('//connect.facebook.net/en_UK/all.js', function(){
        //     FB.init({
        //       appId: '1480111102248684',
        //   });     
            
        // });

        

        // $.validator.addMethod("crossfield",
        //     function(value, element, param) {
        //         $("")
        //     }
        //     );
        
        $(document).on("click", ".modal-footer #btnGetRating", function () {
           
            var selected = [];
            $(".checkbox-inline").find("input[type='checkbox']:checked").each(function(){
                selected.push($(this).attr("value"));
            
            });
            var data = {};
            data["features"] = selected;
            var url = "get-rating.php";
             $.ajax({    
                url: url,
                data: data,
                dataType : "json",
                type: 'GET',
                success: function(output) {
                    processRatings(output);
                },
            //     error:  function(){
            //         console.log("Output error");
            //     }   
        });
            
            
            
             
            
        });

});

    // call web server and get the json data
    function getJSONData(e) {
        if($("#search-form").valid()) {
            var id = ($(e.target)).attr('id');
            
            //fill parameters for ajax get
            var url = "search-ebay.php";
            var data2 = {};
            data2["keywords"] = document.getElementById("inputKeyWords").value;
            
            $.ajax({    
                url: url,
                data: data2,
                dataType : "json",
                type: 'GET',
                success: function(output) {
                    processData(output);
                },
            //     error:  function(){
            //         console.log("Output error");
            //     }   
        });
            

            
            console.log("Form submitted");
            return false;
        }
        else {
            console.log("not Done");
            return false;
        }
    }

    function processData(output) {
        if(output.resultCount == 0) {
            document.getElementById('results').style.display = "none";
            document.getElementById('failure').style.display = "inline";
            return;
        }
        document.getElementById('failure').style.display = "none";
        addResults(output);
        document.getElementById('results').style.display = "inline";
        $(".viewdetails").click(getFeatures);
    }
    
    function addResults(output) {

        document.getElementById('resultset').innerHTML = '';
        var count = 0;
        var itemArray = [];
        for(var key in output) {
            if(key.substring(0,4) == "item" && key != "itemCount") {
                // addMediaObj(output[key], count);
                var thisI = output[key];
                count++;
                itemArray.push(output[key]);
            }
        }
        var result = resultTemplate({items:itemArray});
        $("#resultset").html(result);
    }

    function getFeatures(e) {

        var id = ($(e.target)).attr('id');
        
        id = id.charAt(2).toString();
        $("#load" + id).css("display", "inline");
        $("#modal" + id).modal();
        $("#features" + id).html("");
        var url = 'process-reviews.php';
        var data2 = {};
        data2["productidvalue"] = $("#prdid" + id).text();
        data2["productidType"] = "Reference";
        $.ajax({    
                url: url,
                data: data2,
                dataType : "json",
                type: 'GET',
                success: function(output) {
                    processReviews(output, id);
                },
            //     error:  function(){
            //         console.log("Output error");
            //     }   
        });
    }

    function processReviews(output, id) {
        
        
        // $("#prdid" + id).css("display", "inline");
        // $("#prdid" + id).text(JSON.stringify(output));

        var features = output["list"];
        var result = featureTemplate({items:features});
        $("#features" + id).html(result);
        // $("#features").css("display", "inline");
        $("#load" + id).css("display", "none");
    }



})(jQuery, window, document);
