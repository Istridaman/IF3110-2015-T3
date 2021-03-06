 function validateQuestionForm() {
        var re = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
        if (document.forms["createquestion"]["createname"].value == null || document.forms["createquestion"]["createname"].value == "" ||
            document.forms["createquestion"]["createemail"].value == null || document.forms["createquestion"]["createemail"].value == "" ||
            document.forms["createquestion"]["createtopic"].value == null || document.forms["createquestion"]["createtopic"].value == "" ||
            document.forms["createquestion"]["createcontent"].value == null || document.forms["createquestion"]["createcontent"].value == "") {
            alert("All required fields must be filled out");
            return false;
        }
        else if(!re.test(document.forms["createquestion"]["createemail"].value)){
            alert("Incorrect email address");
            return false;
        }
    }
function validateAnswerForm(){
        var re = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
        if (document.forms["createquestion"]["createname"].value == null || document.forms["createquestion"]["createname"].value == "" ||
            document.forms["createquestion"]["createemail"].value == null || document.forms["createquestion"]["createemail"].value == "" ||
            document.forms["createquestion"]["createcontent"].value == null || document.forms["createquestion"]["createcontent"].value == "") {
            alert("All required fields must be filled out");
            return false;
        }
        else if(!re.test(document.forms["createquestion"]["createemail"].value)){
            alert("Incorrect email address");
            return false;
        }    
}

function vote(id, change, db) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            if (db=="question") { //question
                document.getElementById("vote-q").innerHTML = xhttp.responseText;
            } else { //answer
                document.getElementById("vote-a"+id).innerHTML = xhttp.responseText;
            }
        }
    }
    xhttp.open("GET", "vote?id="+id+"&vote="+change+"&db="+db, true);
    xhttp.send();
} 

function showCommentForm(){
    var div = document.getElementById('savecomment');
    if (div.style.display !== 'none') {
        div.style.display = 'none';
    }
    else {
        div.style.display = 'block';
    }
}