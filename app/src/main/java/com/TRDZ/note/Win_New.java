package com.TRDZ.note;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import static com.TRDZ.note.MainActivity.data;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class Win_New extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
	Button B_date;
	Button B_ok;
	EditText E_name;
	EditText E_cont;
	CheckBox type_1;
	CheckBox type_2;
	CheckBox type_3;
	CheckBox type_4;

	static final String NEW_INDEX = "INDEX";
	static final String NEW_TEXT = "TEXT";
	static final String NEW_ID = "ID";
	static int id;

	/**
	 * Создание фрамента с указанием макета
	 *
	 * @param inflater           Инфлатер
	 * @param container          Визуальный контейнер
	 * @param savedInstanceState Бекап при прирывании
	 * @return Экран
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.win_new, container, false);
		}

	/**
	 * Наполнение контентом
	 *
	 * @param view               Экран
	 * @param savedInstanceState Бекап при прирывании
	 */
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		try { Bundle arguments = requireArguments();
			id = arguments.getInt(NEW_ID);
			TextView tek = view.findViewById(R.id.E_name);
			tek.setText(arguments.getString(NEW_TEXT));
			type_1=view.findViewById(R.id.S_1);
			type_2=view.findViewById(R.id.S_2);
			type_3=view.findViewById(R.id.S_3);
			type_4=view.findViewById(R.id.S_4);
			switch (arguments.getInt(NEW_INDEX)){
			case 0:	type_1.setChecked(true);
				break;
			case 1:	type_2.setChecked(true);
				break;
			case 2:	type_3.setChecked(true);
				break;
			case 3:	type_4.setChecked(true);
				break;
				}
			create_buttons(view);
			}
		catch (IllegalStateException ignored) {}
		}

	public void create_buttons(View view) {
		B_date=view.findViewById(R.id.B_date);
		B_ok=view.findViewById(R.id.B_ok);
		E_name=view.findViewById(R.id.E_name);
		E_cont=view.findViewById(R.id.E_content);
		B_date.setOnClickListener(this);
		B_ok.setOnClickListener(this);
		type_1.setOnClickListener(this);
		type_2.setOnClickListener(this);
		type_3.setOnClickListener(this);
		type_4.setOnClickListener(this);
		}

	/**
	 * Фабричный метод создания фрагмента
	 * @param index ID записи
	 * @return Окно содержимого
	 */
	public static Win_New newInstance(int index, String text, int id) {
		Win_New fragment = new Win_New();
		Bundle args = new Bundle();
		args.putInt(NEW_ID, id);
		args.putInt(NEW_INDEX, index);
		args.putString(NEW_TEXT, text);
		fragment.setArguments(args);
		return fragment;
		}

	@Override
	public void onClick(View view) {
		Button B_pressed = (Button) view;
		if (B_pressed.getId()==R.id.B_date) {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
				{ DatePickerDialog forse = new DatePickerDialog(requireContext(),this,
						Calendar.getInstance().get(Calendar.YEAR),
						Calendar.getInstance().get(Calendar.MONTH),
						Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
				forse.show();}
			}
		if (E_name.getText().toString().equals("")) {
			type_1.setChecked(false);
			type_2.setChecked(false);
			type_3.setChecked(false);
			type_4.setChecked(false);
			return;
			}
		if (id==-1) id=data.add(E_name.getText().toString());
		switch (B_pressed.getId()) {
		case R.id.S_1:
			type_2.setChecked(false);
			type_3.setChecked(false);
			type_4.setChecked(false);
			data.set_type(id,0);
			break;
		case R.id.S_2:
			type_1.setChecked(false);
			type_3.setChecked(false);
			type_4.setChecked(false);
			data.set_type(id,1);
			break;
		case R.id.S_3:
			type_1.setChecked(false);
			type_2.setChecked(false);
			type_4.setChecked(false);
			data.set_type(id,2);
			break;
		case R.id.S_4:
			type_1.setChecked(false);
			type_2.setChecked(false);
			type_3.setChecked(false);
			data.set_type(id,3);
			break;
		case R.id.B_ok:
			data.set_line(id,E_name.getText().toString());
			data.set_cont(id,E_cont.getText().toString());
			MainActivity.save(getContext());
			getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,Win_List.newInstance()).commit();
			}
		}
	@Override
	public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
		B_date.setText(i%100+"/"+i1+1+"/"+i2);
		if (E_name.getText().toString().equals("")) E_name.setText("День");
		if (id==-1) id=data.add(E_name.getText().toString(),0,i,i1,i2);
		else data.set_time(id,i,i1,i2);
		}
	}

