package com.example.midexamapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText etName, etStudentId;
    CheckBox cbEnrolled;
    Button btnAdd, btnView, btnEdit, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Memanggil kelas DatabaseHelper yang sudah Anda buat
        myDb = new DatabaseHelper(this);

        // Menghubungkan variabel dengan ID yang ada di activity_main.xml
        etName = findViewById(R.id.etStudentName);
        etStudentId = findViewById(R.id.etStudentId);
        cbEnrolled = findViewById(R.id.cbEnrolled);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        // Menjalankan fungsi-fungsi tombol
        AddData();
        ViewAll();
        UpdateData();
        DeleteData();
    }

    public void AddData() {
        btnAdd.setOnClickListener(v -> {
            int isEnrolled = cbEnrolled.isChecked() ? 1 : 0;
            boolean isInserted = myDb.insertData(etName.getText().toString(), etStudentId.getText().toString(), isEnrolled);
            if (isInserted) {
                Toast.makeText(MainActivity.this, "Data Tersimpan", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Data Gagal Disimpan", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void ViewAll() {
        btnView.setOnClickListener(v -> {
            Cursor res = myDb.getAllData();
            if (res.getCount() == 0) {
                showMessage("Info", "Tidak ada data ditemukan");
                return;
            }
            StringBuilder buffer = new StringBuilder();
            while (res.moveToNext()) {
                buffer.append("Name : ").append(res.getString(1)).append("\n");
                buffer.append("Student ID : ").append(res.getString(2)).append("\n");
                buffer.append("Enrolled : ").append(res.getInt(3) == 1 ? "Yes" : "No").append("\n\n");
            }
            showMessage("Data Mahasiswa", buffer.toString());
        });
    }

    public void UpdateData() {
        btnEdit.setOnClickListener(v -> {
            int isEnrolled = cbEnrolled.isChecked() ? 1 : 0;
            boolean isUpdate = myDb.updateData(etName.getText().toString(), etStudentId.getText().toString(), isEnrolled);
            if (isUpdate) {
                Toast.makeText(MainActivity.this, "Data Diperbarui", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Data Gagal Diperbarui", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void DeleteData() {
        btnDelete.setOnClickListener(v -> {
            Integer deletedRows = myDb.deleteData(etStudentId.getText().toString());
            if (deletedRows > 0) {
                Toast.makeText(MainActivity.this, "Data Dihapus", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Data Gagal Dihapus", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Fungsi untuk menampilkan pop-up dialog data
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}