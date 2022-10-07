package br.com.juliancambraia.controllers;

import br.com.juliancambraia.converters.NumberConverter;
import br.com.juliancambraia.exceptions.UnsuportedMathOperationException;
import br.com.juliancambraia.math.SimpleMath;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {
    private static final String template = "Hello, %s ";
    private final AtomicLong counter = new AtomicLong();
    private SimpleMath math = new SimpleMath();

    @GetMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo) {

        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric as value");
        }
        return math.sum(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @GetMapping("/subtraction/{numberOne}/{numberTwo}")
    public Double subtraction(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo) {

        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric as value");
        }
        return math.subtraction(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @GetMapping("/multiply/{numberOne}/{numberTwo}")
    public Double multiply(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo) {

        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric as value");
        }
        return math.multiply(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @GetMapping("/division/{numberOne}/{numberTwo}")
    public Double division(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo) {

        if (NumberConverter.isZeroNumber(numberTwo)) {
            throw new ArithmeticException("Division by Zero not allowed");
        }

        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric as value");
        }

        return math.division(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @GetMapping("/mean/{numberOne}/{numberTwo}")
    public Double mean(
            @PathVariable(value = "numberOne") String numberOne,
            @PathVariable(value = "numberTwo") String numberTwo) {

        if (NumberConverter.isZeroNumber(numberTwo)) {
            throw new ArithmeticException("Division by Zero not allowed");
        }

        if (!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo)) {
            throw new UnsuportedMathOperationException("Please set a numeric as value");
        }

        return math.mean(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @GetMapping("/squarRoot/{number}")
    public Double squarRoot(@PathVariable(value = "number") String number) {

        if (!NumberConverter.isNumeric(number)) {
            throw new UnsuportedMathOperationException("Please set a numeric as value");
        }

        return math.squarRoot(NumberConverter.convertToDouble(number));
    }

}
