package br.senac.sp.l13.appheroephpmysql;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText editTextHeroId, editTextName, editTextDesejo;
    RatingBar ratingBar;
    Spinner spinnerPrioridade;
    ProgressBar progressBar;
    ListView listView;
    Button buttonAddUpdate;

    List<Desejo> heroList;

    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextHeroId = findViewById(R.id.editTextDesejoId);
        editTextName = findViewById(R.id.editTextName);
        editTextDesejo = findViewById(R.id.editTextDesejo);
        spinnerPrioridade = findViewById(R.id.spinnerPrioridade);

        buttonAddUpdate = findViewById(R.id.buttonAddUpdate);

        progressBar = findViewById(R.id.progressBar);

        listView = findViewById(R.id.listViewHeroes);

        heroList = new ArrayList<>();

        buttonAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isUpdating) {
                    updateHero();
                } else {
                    createHero();
                }

            }
        });

        readHeroes();
    }

    private void createHero() {
        String name = editTextName.getText().toString().trim();
        String desejo = editTextDesejo.getText().toString().trim();
        String prioridade = spinnerPrioridade.getSelectedItem().toString();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Por favor entre com o nome");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(desejo)) {
            editTextDesejo.setError("Por favor entre com o nome real");
            editTextDesejo.requestFocus();
            return;
        }

        //Conexão entre o Android e o PHP através do Hash.
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("desejo", desejo);
        params.put("prioridade", prioridade);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_DESEJO, params, CODE_POST_REQUEST);
        request.execute();
    }

    private void readHeroes() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_DESEJOS, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void updateHero() {
        String id = editTextHeroId.getText().toString();
        String name = editTextName.getText().toString().trim();
        String desejo = editTextDesejo.getText().toString().trim();
        String prioridade = spinnerPrioridade.getSelectedItem().toString();


        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Por favor entre com o nome");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(desejo)) {
            editTextDesejo.setError("Descreva seu Desejo");
            editTextDesejo.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("desejo", desejo);
        params.put("prioridade", prioridade);


        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_DESEJO, params, CODE_POST_REQUEST);
        request.execute();

        buttonAddUpdate.setText("Adicionar");

        editTextName.setText("");
        editTextDesejo.setText("");
        spinnerPrioridade.setSelection(0);

        isUpdating = false;
    }

    private void deleteHero(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_DELETE_DESEJO + id, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshHeroList(JSONArray heroes) throws JSONException {
        heroList.clear();

        for (int i = 0; i < heroes.length(); i++) {
            JSONObject obj = heroes.getJSONObject(i);

            heroList.add(new Desejo(
                    obj.getInt("id"),
                    obj.getString("name"),
                    obj.getString("desejo"),
                    obj.getString("prioridade")
            ));
        }

        DesejoAdapter adapter = new DesejoAdapter(heroList);
        listView.setAdapter(adapter);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshHeroList(object.getJSONArray("desejos"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class DesejoAdapter extends ArrayAdapter<Desejo> {
        List<Desejo> desejoList;

        public DesejoAdapter(List<Desejo> desejoList) {
            super(MainActivity.this, R.layout.layout_desejo_list, desejoList);
            this.desejoList = desejoList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_desejo_list, null, true);

            TextView textViewName = listViewItem.findViewById(R.id.textViewName);

            TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Desejo desejo = desejoList.get(position);

            textViewName.setText(desejo.getName());

            textViewUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isUpdating = true;
                    editTextHeroId.setText(String.valueOf(desejo.getId()));
                    editTextName.setText(desejo.getName());
                    editTextDesejo.setText(desejo.getDesejo());
                    spinnerPrioridade.setSelection(((ArrayAdapter<String>) spinnerPrioridade.getAdapter()).getPosition(desejo.getPrioridade()));
                    buttonAddUpdate.setText("Alterar");
                }
            });

            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Apagar " + desejo.getName())
                            .setMessage("Tem certeza que deseja exluir?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteHero(desejo.getId());
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

            return listViewItem;
        }
    }
}
