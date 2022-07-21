package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class ViewController implements Initializable {
	@FXML
	private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btnReset;
	@FXML
	private VBox grid;
	@FXML
	private Line line;
	private List<Button> btns;
	private int vez;
	private int index = 0;
	private int jogada = 0;
	private String[][] matriz;
	private List<List<String>> probabilidades;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		vez = (int) Math.round((Math.random() * 1));
		btns = Arrays.asList(btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9);
		matriz = new String[3][3];
		for (int x = 0; x < 3; x++)
			for (int y = 0; y < 3; y++)
				btns.get(index++).setId(x + ";" + y);

		btns.forEach((btn) -> {
			btn.setOnMouseClicked((e) -> {
				handleClick(getButton(btn.getId()));
			});
		});

		probabilidades = new ArrayList<>(8);
		index = 0;
		probabilidades.add(index++, Arrays.asList("0;0", "0;1", "0;2")); // linha 0
		probabilidades.add(index++, Arrays.asList("1;0", "1;1", "1;2")); // linha 1
		probabilidades.add(index++, Arrays.asList("2;0", "2;1", "2;2")); // linha 2

		probabilidades.add(index++, Arrays.asList("0;0", "1;0", "2;0")); // coluna 0
		probabilidades.add(index++, Arrays.asList("0;1", "1;1", "2;1")); // coluna 1
		probabilidades.add(index++, Arrays.asList("0;2", "1;2", "2;2")); // coluna 2

		probabilidades.add(index++, Arrays.asList("0;0", "1;1", "2;2")); // [\]
		probabilidades.add(index++, Arrays.asList("0;2", "1;1", "2;0")); // [/]

		clean();
	}

	private void actionLine(List<String> result) {
		int i = 0;
		for (; i < probabilidades.size(); i++) {
			var lista = probabilidades.get(i);
			if (lista.equals(result))
				break;
		}
		line.setVisible(true);
		if (i == 0)
			line.setTranslateY(-140);
		else if (i == 1)
			line.setTranslateY(0);
		else if (i == 2)
			line.setTranslateY(140);
		else if (i == 3) {
			line.setTranslateX(-134);
			line.setRotate(90);
		} else if (i == 4)
			line.setRotate(90);
		else if (i == 5) {
			line.setRotate(90);
			line.setTranslateX(134);
		} else if (i == 6)
			line.setRotate(45);
		else if (i == 7)
			line.setRotate(-45);
	}

	public void handleClick(Button btn) {
		if (btn.getText().isEmpty()) {
			setJogada(btn);
			if (jogada >= 5) {
				var result = checkWin(btn);
				if (!(result == null)) {
					System.out.println("O jogador " + btn.getText() + " ganhou!!!");
					grid.setDisable(true);
					actionLine(result);
				}
			}
		}
	}

	private void setJogada(Button btn) {
		jogada++;
		String j = gerarVez();
		btn.setText(j);
		btn.getStyleClass().add((j.equals("X") ? "X" : "O"));
		Point p = new Point(btn.getId());
		matriz[p.x][p.y] = j;
	}

	public List<String> checkWin(Button btn) {
		String value = btn.getId();
		var results = new ArrayList<List<String>>(); // ver se tem probabilidade
		for (var lista : probabilidades) {
			if (lista.contains(value)) {
				results.add(lista);
			}
		}
		value = btn.getText();

		for (var lista : results) {
			var p1 = new Point(lista.get(0));
			if (matriz[p1.x][p1.y] == value) {
				var p2 = new Point(lista.get(1));
				if (matriz[p2.x][p2.y] == value) {
					var p3 = new Point(lista.get(2));
					if (matriz[p3.x][p3.y] == value) {
						return lista;
					}
				}
			}
		}
		return null;
	}

	private Button getButton(String coordenada) {
		Button btn = null;
		for (var b : btns) {
			if (b.getId().equals(coordenada)) {
				btn = b;
				break;
			}
		}
		return btn;
	}

	public void clean() {
		btns.forEach((btn) -> {
			btn.setText("");
			btn.getStyleClass().removeAll("X", "O");
		});
		jogada = 0;
		matriz = new String[3][3];
		grid.setDisable(false);
		line.setVisible(false);
		line.setRotate(0);
		line.setTranslateX(0);
		line.setTranslateY(0);
	}

	public String gerarVez() {
		if (vez % 2 == 0) {
			vez = 1;
			return "X";
		}
		vez = 0;
		return "O";
	}
}

class Point {
	public int x;
	public int y;

	Point(String value) {
		var point = value.split(";");
		this.x = Integer.parseInt(point[0]);
		this.y = Integer.parseInt(point[1]);
	}
}
