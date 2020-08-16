package com.vornetran.marc.wechselspiel.logic;

import java.awt.Color;

import com.vornetran.marc.wechselspiel.Wechselspiel;
import com.vornetran.marc.wechselspiel.data.ComboCheckResult;
import com.vornetran.marc.wechselspiel.data.Constants;
import com.vornetran.marc.wechselspiel.data.Field;
import com.vornetran.marc.wechselspiel.util.ColorUtil;

public class Spielfeld {

	private final Field[][] fields = new Field[Constants.FIELD_SIZE][Constants.FIELD_SIZE];
	private Field selectedField = null;
	private boolean foundCombo = false;
	
	public Spielfeld(){
		for(int y = 0; y < fields.length; y++){
			for(int x = 0; x < fields.length; x++){
				generateNewField(x, y);
			}
		}
	}
	
	public Field[][] getFields(){
		return fields;
	}
	
	public boolean isSelectedField(Field field){
		return field == selectedField;
	}
	
	public void clickField(int x, int y){
		Field field = fields[x][y];
		
		if(isSelectedField(field)){
			this.selectedField = null;
		}else{
			if(selectedField != null){
				if(!field.isNeighbor(selectedField)){
					this.selectedField = null;
					
					return;
				}
				
				if(canSwitchFields(selectedField, field)){
					switchFields(selectedField, field);
					countPointsAndRemoveMatches();
					
					this.selectedField = null;
				}
			}else{
				this.selectedField = field;
			}
		}
	}
	
	public Field getField(int x, int y){
		return fields[x][y];
	}
	
	public boolean canSwitchFields(Field field1, Field field2){
		if(countSameColoredNeighbors(field1, field2.getColor()) >= 2)return true;
		if(countSameColoredNeighbors(field2, field1.getColor()) >= 2)return true;
		
		return false;
	}
	
	// public
	// private
	
	private void generateNewField(int x, int y){
		Color color;
		
		do{
			color = ColorUtil.getRandomColor();
		}while(!isValidInitialColor(color, x, y));
		
		fields[x][y] = new Field(x, y, color);
	}
	
	private void switchFields(Field field1, Field field2){
		fields[field1.getX()][field1.getY()] = field2;
		fields[field2.getX()][field2.getY()] = field1;
		
		field1.switchWith(field2);
	}
	
	private boolean isValidInitialColor(Color color, int x, int y){
		if(hasSameColoredNeighbors(color, x, y, -1, 0))return false;
		if(hasSameColoredNeighbors(color, x, y, 1, 0))return false;
		if(hasSameColoredNeighbors(color, x, y, 0, -1))return false;
		if(hasSameColoredNeighbors(color, x, y, 0, 1))return false;
		
		return true;
	}
	
	private boolean hasSameColoredNeighbors(Color color, int x, int y, int xStep, int yStep){
		for(int i = 0; i < 2; i++){
			x += xStep;
			y += yStep;
			
			if(!checkCoordinates(x, y)){
				return false;
			}
			
			Field field = fields[x][y];
			
			if(field == null || !field.hasSameColor(color)){
				return false;
			}
		}
		
		return true;
	}
	
	private Field getRelativeField(Field field, int modX, int modY){
		int x = field.getX() + modX;
		int y = field.getY() + modY;
		
		if(checkCoordinates(x, y)){
			return fields[x][y];
		}else{
			return null;
		}
	}
	
	private boolean hasRelativeFieldSameColor(Field field, Color color, int modX, int modY){
		Field f = getRelativeField(field, modX, modY);
		
		if(f != null){
			return f.hasSameColor(color);
		}else{
			return false;
		}
	}
	
	private int countSameColoredNeighborsHorizontal(Field field, Color color){
		int count = 0;
		
		for(int i = 1; i <= 2; i++){
			if(hasRelativeFieldSameColor(field, color, -i, 0)){
				count++;
			}else{
				break;
			}
		}
		
		for(int i = 1; i <= 2; i++){
			if(hasRelativeFieldSameColor(field, color, i, 0)){
				count++;
			}else{
				break;
			}
		}
		
		return count;
	}
	
	private int countSameColoredNeighborsVertical(Field field, Color color){
		int count = 0;
		
		for(int i = 1; i <= 2; i++){
			if(hasRelativeFieldSameColor(field, color, 0, -i)){
				count++;
			}else{
				break;
			}
		}
		
		for(int i = 1; i <= 2; i++){
			if(hasRelativeFieldSameColor(field, color, 0, i)){
				count++;
			}else{
				break;
			}
		}
		
		return count;
	}
	
	private int countSameColoredNeighbors(Field field, Color color){
		int horizontal = countSameColoredNeighborsHorizontal(field, color);
		
		if(horizontal >= 2){
			return horizontal;
		}
		
		int vertical = countSameColoredNeighborsVertical(field, color);
		
		if(vertical >= 2){
			return vertical;
		}
		
		return 0;
	}
	
	private boolean checkCoordinates(int x, int y){
		return x >= 0 && y >= 0 && x < fields.length && y < fields.length;
	}
	
	private void countPointsAndRemoveMatches(){
		countPointsAndRemoveMatches(true);
	}
	
	private void countPointsAndRemoveMatches(boolean isFirstPass){
		this.foundCombo = false;
		
		for(int y = 0; y < fields.length; y++){
			Color currentColor = null;
			int combo = 0;
			
			for(int x = 0; x < fields.length; x++){
				ComboCheckResult result = doComboCheck(x, y, currentColor, combo, true);
				currentColor = result.getCurrentColor();
				combo = result.getCombo();
			}
			
			if(combo >= 3){
				backtrackComboAndAddPoints(fields.length, y, combo, true);
			}
		}
		
		for(int x = 0; x < fields.length; x++){
			Color currentColor = null;
			int combo = 0;
			
			for(int y = 0; y < fields.length; y++){
				ComboCheckResult result = doComboCheck(x, y, currentColor, combo, false);
				currentColor = result.getCurrentColor();
				combo = result.getCombo();
			}
			
			if(combo >= 3){
				backtrackComboAndAddPoints(x, fields.length, combo, false);
			}
		}
		
		applyGravityToFields();
		fillEmptyFields();
		
		if(isFirstPass || foundCombo){
			countPointsAndRemoveMatches(false);
		}
	}
	
	private ComboCheckResult doComboCheck(int x, int y, Color currentColor, int combo, boolean isHorizontal){
		Field field = fields[x][y];
		Color fieldColor = field.getColor();
		
		if(fieldColor == currentColor){
			combo++;
		}else{
			if(combo >= 3){
				backtrackComboAndAddPoints(x, y, combo, isHorizontal);
			}
			
			combo = 1;
			currentColor = fieldColor;
		}
		
		return new ComboCheckResult(currentColor, combo);
	}
	
	private void backtrackComboAndAddPoints(int x, int y, int combo, boolean isHorizontal){
		this.foundCombo = true;
		
		backtrackCombo(x, y, combo, isHorizontal);
		Wechselspiel.getWechselspiel().addPoints(combo * 10);
	}
	
	private void backtrackCombo(int x, int y, int combo, boolean isHorizontal){
		int modX;
		int modY;
		
		if(isHorizontal){
			modX = -1;
			modY = 0;
		}else{
			modX = 0;
			modY = -1;
		}
		
		backtrackCombo(x, y, modX, modY, combo);
	}
	
	private void backtrackCombo(int x, int y, int modX, int modY, int combo){
		for(int i = 0; i < combo; i++){
			x += modX;
			y += modY;
			fields[x][y].shouldBeRemoved();
		}
	}
	
	private void applyGravityToFields(){
		for(int y = fields.length - 1; y > 0; y--){
			for(int x = fields.length - 1; x >= 0; x--){
				Field field = fields[x][y];
				
				if(field.shouldThisBeRemoved()){
					letFieldsAboveFallDown(x, y - 1);
				}
			}
		}
	}
	
	private void letFieldsAboveFallDown(int x, int y){
		for(int i = y; i >= 0; i--){
			letFieldFallDown(x, i);
		}
	}
	
	private void letFieldFallDown(int x, int y){
		while(y < fields.length - 1){
			Field field = fields[x][y];
			Field bottomField = fields[x][y+1];
			
			if(bottomField.shouldThisBeRemoved()){
				switchFields(field, bottomField);
			}else{
				break;
			}
			
			y++;
		}
	}
	
	private void fillEmptyFields(){
		for(int y = 0; y < fields.length; y++){
			for(int x = 0; x < fields.length; x++){
				Field field = fields[x][y];
				
				if(field.shouldThisBeRemoved()){
					generateNewField(x, y);
				}
			}
		}
	}
	
}
