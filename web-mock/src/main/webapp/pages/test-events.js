(function (doc) {

  /**
   * Add or remove events you are interested in....
   */
  var events = ['click', 'blur', 'focus', 'change'];

  for (i = 0; i <= events.length; i++) {
    doc.addEventListener(events[i], eventCallback, true);
  }

  function eventCallback(ev) {
    var reportList = document.getElementById('report');
    var lineReport = ev.type + ' event has fired on ' + ev.target.tagName + ' Capture success.';
    var listElem = document.createElement('li');

    listElem.appendChild(document.createTextNode(lineReport));
    reportList.appendChild(listElem)

    console.log(lineReport);
  }

})(document)