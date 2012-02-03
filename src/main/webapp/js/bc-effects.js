$(document).ready(function() {

	// Workaround because Lift doesnt support any DOM manipulation
	$(":button").button();
	$("#bc_dashboard").tabs();
	$(".bc_panel").tabs();
	$("#txt_wager").live('keyup', function(e) {
		  $(this).val($(this).val().replace(/[^0-9]/g, ''));
		});
	
	$("#bc_ticket").bind("DOMNodeInserted", function() {
		if($(".bet_entry").length >= 3) {
			$("#bt_calculate").button("option", "disabled", false);
			$('#txt_wager').removeAttr('disabled');
			$('#select_system').removeAttr('disabled');
		}
	});
	
	$("#bt_clear").bind("click", function() {
		$("#bt_calculate").button("option", "disabled", true);
		$('#txt_wager').attr('disabled','disabled');
		$('#select_system').attr('disabled','disabled');
	});
	
	$("#bt_help").button( "option", "icons", {primary:'ui-icon-help'} );
	$("#bt_help").bind("click", function() {
		window.open("http://de.wikipedia.org/wiki/Sportwette#Systemwette","BetCalculator>Help", "width=800,height=650,menubar=1,scrollbars=0,status=1,toolbar=1,resizable=0");
        return false;
	});

});