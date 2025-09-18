function verificaForcaSenha() {
    var numeros = /([0-9])/;
    var alfabeto = /([a-zA-Z])/;
    var chEspeciais = /([~,!,@,#,$,%,^,&,*,-,_,+,=,?,>,<])/;

    if ($('#senha_usuario').val().length < 8) {
        $('#senha-status').html("<span style='color:red'>Fraco, insira no mínimo 8 caracteres</span>");
    } else {
        if ($('#senha_usuario').val().match(numeros) && $('#senha_usuario').val().match(alfabeto) && $('#senha_usuario').val().match(chEspeciais)) {
            $('#senha-status').html("<span style='color:green'><b>Forte</b></span>");
        } else {
            $('#senha-status').html("<span style='color:orange'>Médio, insira um caracter especial</span>");
        }
    }
}


