package main;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	private int program;
	private int vs, fs;

	public Shader(String file) {
		program = glCreateProgram();

		vs = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vs, readFile(file + ".vs"));
		glCompileShader(vs);
		if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1){
			System.err.println(glGetShaderInfoLog(vs));
			System.exit(1);
		}
		
		fs = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fs, readFile(file + ".fs"));
		glCompileShader(fs);
		if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1){
			System.err.println(glGetShaderInfoLog(fs));
			System.exit(1);
		}
		
		glAttachShader(program, vs);
		glAttachShader(program, fs);
		
		glBindAttribLocation(program, 0, "vertices");
		glLinkProgram(program);
		if (glGetProgrami(program, GL_LINK_STATUS) != 1){
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
		glValidateProgram(program);
		if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1){
			System.err.println(glGetProgramInfoLog(program));
			System.exit(1);
		}
	}

	private String readFile(String file) {
		StringBuilder string = new StringBuilder();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("./shaders/ + file")));
			String line;
			
			while((line = br.readLine()) != null){
				string.append(line);
				string.append("\n");
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string.toString();
	}
	
	public void bind(){
		glUseProgram(program);
	}
	
}