package com.TRDZ.note.contein;

import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import static com.TRDZ.note.MainActivity.data;
import static com.TRDZ.note.MainActivity.adapter;

import com.TRDZ.note.Executor;
import com.TRDZ.note.MainActivity;
import com.TRDZ.note.R;

public class WindowList extends Fragment implements WindowsListIteration {

    private static final String CURRENT_NOTE = "CURRENT_NOTE";
    private int Current_note = 0;
    private Executor executor;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_win_list,menu);
        super.onCreateOptionsMenu(menu, inflater);
        }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sort) {
            adapter.sort();
            executor.save();
            executor.show_toast(getString(R.string.after_sort),Toast.LENGTH_SHORT);
            }
        return super.onOptionsItemSelected(item);
        }

    /**
     * Создание фрамента с указанием макета
     * @param inflater Инфлатер
     * @param container Визуальный контейнер
     * @param savedInstanceState Бекап при прирывании
     * @return Экран
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.win_list, container, false);
        }

    /**
     * Инициализация после создания и подготовки макета
     * @param savedInstanceState Бекап при прирывании
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        executor = ((MainActivity) getActivity());
        setHasOptionsMenu(true);
        if (savedInstanceState != null) Current_note = savedInstanceState.getInt(CURRENT_NOTE, 0);
        create_list(view);
        if (executor.isLandscape()) { create_info_land(Current_note); }
        create_button(view);
        }

    /**
     * Заполнение списка заметок
     */
    protected void create_list(View view) {
        adapter.set_Iteration(this);
        RecyclerView recyclerView = view.findViewById(R.id.recycle_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        }

//region Взаимодействие с перечнем
    /**
     * Подготовка к созданию окна с информацией
     * @param index Номер выбранной записи
     */
    public void OnClick_create_info(int index) {
        Current_note = index;
        if (executor.isLandscape()) create_info_land(index);
        else create_info_port(index);
        }

    /**
     * Взаимодействие через подробное меню
     * @param index Номер выбранной записи
     */
    public void OnPress_get_iteration(int index,View view) {
        PopupMenu popup = new PopupMenu(requireContext(),view);
        requireActivity().getMenuInflater().inflate(R.menu.popup,popup.getMenu());
        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
            case (R.id.menu_delete):
                new AlertDialog.Builder(requireContext()).setTitle(getString(R.string.deletion)).setMessage(getString(R.string.del_confirm))
                    /*YES*/ .setPositiveButton(getString(R.string.yes), ((dialogInterface, i) -> {executor.deletion(index);}))
                    /*NO*/  .setNegativeButton(getString(R.string.no), ((dialogInterface, i) -> {}))
                    .show();
                break;
            case (R.id.menu_change):
                int segment;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                segment = R.id.fragment_container_second; }
                else segment = R.id.fragment_container;
                WindowNew detail = WindowNew.newInstance(-1, "", index);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(segment, detail);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
                }
            return false;
            });
        popup.show();
        }

    /**
     * Вывод содержимого земетки в порт ориентации
     * @param index номер заметки
     */
    private void create_info_port(int index) {
        WindowText detail = WindowText.newInstance(data.get_type(index),data.get_cont(index), index);
        ((MainActivity) requireActivity()).get_Navigation().replace(R.id.fragment_container,detail,true);
        }

    /**
     * Вывод содержимого земетки в ланд ориентации
     * @param index номер заметки
     */
    private void create_info_land(int index) {
        WindowText detail = WindowText.newInstance(data.get_type(index),data.get_cont(index), index);
        ((MainActivity) requireActivity()).get_Navigation().replace(R.id.fragment_container_second,detail,false);
        }
//endregion

//region Взаимодействие с кнопкой добавления
    /**
     * Подготовка к вызову окна создания новой заметки
     */
    private void create_button(View view) {
        Button b_new = view.findViewById(R.id.B_new);
        b_new.setOnClickListener(view1 -> {
            if (executor.isLandscape()) { create_new_land();}
            else create_new_port();
            });
        }

    /**
     * Вывод окна создяния новой заметки в порт ориентации
     */
    private void create_new_port() {
        WindowNew detail = WindowNew.newInstance(-1,"",-1);
        ((MainActivity) requireActivity()).get_Navigation().replace(R.id.fragment_container,detail,true);
        }

    /**
     * Вывод окна создяния новой заметки в ланд ориентации
     */
    private void create_new_land() {
        WindowNew detail = WindowNew.newInstance(-1,"",-1);
        ((MainActivity) requireActivity()).get_Navigation().replace(R.id.fragment_container_second,detail,false);
        }
//endregion

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_NOTE, Current_note);
        super.onSaveInstanceState(outState);
        }

    }