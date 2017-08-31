package com.example.andrepv.validaboleto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SwitchCompat toggleBoletoComum;
    SwitchCompat toggleLinhaDigitavel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editBoleto = (EditText) findViewById(R.id.edittext_barcode);
        toggleBoletoComum = (SwitchCompat) findViewById(R.id.toggle_boleto_comum);
        toggleLinhaDigitavel = (SwitchCompat) findViewById(R.id.toggle_linha_digitavel);
        toggleBoletoComum.setChecked(true);
        toggleLinhaDigitavel.setChecked(false);
        toggleBoletoComum.setClickable(false);

        final Button button = (Button) findViewById(R.id.button_validar);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String boleto = editBoleto.getText().toString();

                if (toggleBoletoComum.isChecked()) {
                    if (isBoletoValido(boleto)) {
                        Toast.makeText(getBaseContext(), "Boleto é válido", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Boleto inválido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (isLinhaDigitavelValida(boleto)) {
                        Toast.makeText(getBaseContext(), "Linha digitável é válida", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Linha digitável inválida", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        toggleLinhaDigitavel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleBoletoComum.toggle();
                } else {
                    toggleBoletoComum.toggle();
                }
            }
        });
    }


    public boolean isLinhaDigitavelValida(String linhaDigitavel) {
        String multiplierGroup;
        int checkerDigit1;
        int checkerDigit2;
        int checkerDigit3;
        multiplierGroup = "2121212121";

        if (linhaDigitavel.length() == 47) {
            checkerDigit1 = getDigitableLineCheckerDigit(linhaDigitavel, multiplierGroup, 8, -1);
            checkerDigit2 = getDigitableLineCheckerDigit(linhaDigitavel, multiplierGroup, 19, 9);
            checkerDigit3 = getDigitableLineCheckerDigit(linhaDigitavel, multiplierGroup, 30, 20);

            return checkerDigit1 == Character.getNumericValue(linhaDigitavel.charAt(9)) &&
                    checkerDigit2 == Character.getNumericValue(linhaDigitavel.charAt(20)) &&
                    checkerDigit3 == Character.getNumericValue(linhaDigitavel.charAt(31));
        } else {
            return false;
        }
    }


    public boolean isBoletoValido(String boleto) {
        int checkerDigit1;
        if (boleto.length() == 44) {
            checkerDigit1 = getBoletoCheckerDigit(boleto);
            return checkerDigit1 == Character.getNumericValue(boleto.charAt(4));
        } else {
            return false;
        }
    }

    private int getBoletoCheckerDigit(String boleto) {
        int sum;
        String multiplierGroup;
        int mod;
        int checkerDigit1;
        sum = 0;
        multiplierGroup = "2345678923456789234567892345678923456789234";
        for (int i = 43; i > 4; i--) {
            sum += Character.getNumericValue(boleto.charAt(i)) * Character.getNumericValue(multiplierGroup.charAt(43 - i));
        }
        for (int i = 3; i >= 0; i--) {
            sum += Character.getNumericValue(boleto.charAt(i)) * Character.getNumericValue(multiplierGroup.charAt(42 - i));
        }
        mod = sum % 11;
        if (mod == 0 || mod == 1 || mod == 10) {
            checkerDigit1 = 1;
        } else {
            checkerDigit1 = 11 - mod;
        }
        return checkerDigit1;
    }

    private int getDigitableLineCheckerDigit(String boleto, String multiplierGroup, int max, int min) {
        int multiplicationResult;
        int sum = 0;

        for (int i = max; i > min; i--) {
            multiplicationResult = Character.getNumericValue(boleto.charAt(i))
                    * Character.getNumericValue(multiplierGroup.charAt(max - i));
            if (multiplicationResult >= 10) {
                sum += splitGreaterTen(multiplicationResult);
            } else {
                sum += Character.getNumericValue(boleto.charAt(i))
                        * Character.getNumericValue(multiplierGroup.charAt(max - i));
            }
        }
        return calculateDigitableLineCheckerDigit(sum);
    }

    private int calculateDigitableLineCheckerDigit(int sum) {
        int mod;
        int checkerDigit1;
        mod = sum % 10;
        if (mod == 0) {
            checkerDigit1 = 0;
        } else {
            checkerDigit1 = 10 - mod;
        }
        return checkerDigit1;
    }

    public int splitGreaterTen(int num) {
        int sum = 0;
        int number = num;
        while (number > 0) {
            sum = sum + number % 10;
            number = number / 10;
        }
        return sum;
    }

}
