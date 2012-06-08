package gluoncompiler;

import java.util.Collection;

/**
 * Contains library code for including in the assembler output.
 *
 * @author DrLabman
 */
public class GluonLibrary {
	public static String varNameToLabel(String varName){
		return "varname_"+varName;
	}
	
	public static String varToLabel(String varName){
		return "var_"+varName;
	}

	public static String funcToLabel(String funcName){
		return "func_"+funcName;
	}


	public static void printASMStart(GluonOutput output){
		output.code("org 100h");
		output.comment("jump to start of program");
		output.code("JMP start");
		//TODO: include lib stuff like print char functions here

		output.comment("print a number, AX contains the number, BX contains the base");
		output.label("print_number");
		output.code("MOV DX, 0");
		output.code("PUSH 0       ; push 0 on the stack as a marker when printing");
		output.code("MOV ECX, 2147483648");
        output.code("AND ECX, EAX");
        output.code("TEST ECX, ECX");
        output.code("JZ calculate_digit");
        output.code("MOV EDX, 4294967295");
        output.code("SUB EDX, EAX");
        output.code("MOV EAX, EDX");
        output.code("ADD EAX, 1");
        output.code("MOV EDX, 0");
		output.label("calculate_digit");
		output.code("DIV BX       ; divide by base");
		output.code("ADD DX, '0'  ; add '0' char to dx value to get correct char");
		output.code("PUSH DX      ; push the char onto stack for printing later");
		output.code("MOV DX, 0");
		output.code("TEST AX, AX  ; test if there is anything left");
		output.code("JNE calculate_digit ; jump back up to deal with the rest of the number");
		output.comment("print the chars on the stack until we get a zero");
		output.code("MOV AH, 2    ; set ah (print char when int 21h called)");
		output.code("TEST ECX, ECX");
        output.code("JZ print_off_stack");
        output.code("MOV DX, '-'");
        output.code("INT 21h");
		output.label("print_off_stack");
		output.code("POP DX       ; pop a character");
		output.code("TEST DX,DX   ; test if it's a null char");
		output.code("JZ num_end   ; exit if null");
		output.code("INT 21h      ; otherwise print char");
		output.code("JMP print_off_stack");
		output.label("num_end");
		output.code("MOV DX,0Dh   ; print CR");
		output.code("INT 21h");
		output.code("MOV DX,0Ah   ; print LF");
		output.code("INT 21h");
		output.code("RET");
		output.comment("end of print number");
		output.code("");
		output.comment("print a null terminated string, input in AX");
		output.label("print_string");
		output.code("MOV BX, AX");
		output.code("MOV AH, 2");
		output.label("char_print_loop");
		output.code("MOV DL, [BX]");
		output.code("INC BX");
		output.code("TEST DX,DX");
		output.code("JZ char_print_end");
		output.code("INT 21h");
		output.code("JMP char_print_loop");
		output.label("char_print_end");
		output.code("MOV DX,0Dh   ; print CR");
		output.code("INT 21h");
		output.code("MOV DX,0Ah   ; print LF");
		output.code("INT 21h");
		output.code("RET");
		output.comment("end of print_string");
		output.code("");
		output.comment("start of program");
	}

	public static void printASMEnd(GluonOutput output){
		output.code("start:");
		output.code("CALL main");
		output.code(";Dos Exit Call");
		output.code("MOV AX,4C00h");
		output.code("INT 21h");
	}

	public static void printVariableFunction(GluonOutput output){
		output.comment("; print all vars");
		output.code("variable EQU EBP+6");
		output.label("print");
        output.code("PUSH EBP");
        output.code("MOV EBP, ESP");
        output.code("PUSH EBX");
        output.code("PUSH EAX");
		output.code("MOV EAX, [variable]");
		output.code("MOV EBX, 10");
		output.code("CALL print_number");
		output.code("POP EAX");
        output.code("POP EBX");
        output.code("POP EBP");
		output.code("RET");
	}
}
