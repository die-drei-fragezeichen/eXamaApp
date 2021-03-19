$(function() {
    $("#datepicker").datepicker();

    $("#datepicker2").datepicker({
        dateFormat: "yy-mm-dd"
    });

    $("#datepicker3").datepicker({
        changeMonth: true,
        changeYear: true
    });
    $("#datepicker4").datepicker({
        //For today use minDate:0
        minDate: 0,
        maxDate: "+1M +5D"
    });

    //The following example shows how you can implement date range select functionality in the form input field. This code allows the user to select From Date and To Date, but it restricts the user to select a previous date as To Date.
    var from = $("#fromDate")
        .datepicker({
            dateFormat: "yy-mm-dd",
            changeMonth: true
        })
        .on("change", function() {
            to.datepicker("option", "minDate", getDate(this));
        }),
        to = $("#toDate").datepicker({
            dateFormat: "yy-mm-dd",
            changeMonth: true
        })
        .on("change", function() {
            from.datepicker("option", "maxDate", getDate(this));
        });

    function getDate(element) {
        var date;
        var dateFormat = "yy-mm-dd";
        try {
            date = $.datepicker.parseDate(dateFormat, element.value);
        } catch (error) {
            date = null;
        }

        return date;
    }
    //datepicker 5
    //here are some static values
    var semesterStart = new Date(2021, 0, 11),
        semesterEnd = new Date(2021, 2, 14);
    holidayStart = new Date(2021, 1, 7),
        holidayEnd = new Date(2021, 1, 14),
        $("#datepicker5").datepicker({
            //defines how many months are shown
            numberOfMonths: 1,
            beforeShowDay: function(date) {
                return [(date >= semesterStart && date <= holidayStart || date >= holidayEnd &&
                    date <= semesterEnd), ''];
            },
            //Upper & lower bounds
            minDate: semesterStart,
            maxDate: semesterEnd
        });




});