$(document).ready(function() {

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
//	$("#select_gamedays").change(function() {
//		$('#select_games').removeAttr('disabled');
//	})
//	$("#select_games").change(function() {
//		$('#select_bets').removeAttr('disabled');
//	})
//	$("#select_bets").change(function() {
//		$("#bt_addbet").removeAttr("disabled");
//		$("#bt_addbet").button("option", "disabled", "false");
//	})

});