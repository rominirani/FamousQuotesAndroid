package com.mindstorm.famousquotesandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.mindstorm.famousquotes.entity.quoteendpoint.Quoteendpoint;
import com.mindstorm.famousquotes.entity.quoteendpoint.model.Quote;

public class AddQuoteActivity extends Activity {
	
	EditText editAuthorName;
	EditText editMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addquote);
		
		editAuthorName = (EditText)findViewById(R.id.editAuthorName);
		editMessage = (EditText)findViewById(R.id.editMessage);
		
		//Event Listener for About App button
		Button btnAddQuote = (Button)findViewById(R.id.btnAddQuote);
		btnAddQuote.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Check if values are provided
				String txtAuthorName = editAuthorName.getText().toString().trim();
				String txtMessage = editMessage.getText().toString().trim();
				
				if ((txtAuthorName.length() == 0) || (txtMessage.length() == 0)) {
					Toast.makeText(AddQuoteActivity.this, "You need to provide values for Author and Message", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//Go ahead and perform the transaction
				String[] params = {txtAuthorName,txtMessage};
				new AddQuoteAsyncTask(AddQuoteActivity.this).execute(params);
				
			}
		});
		
	}
	
	private class AddQuoteAsyncTask extends AsyncTask<String, Void, Quote>{
		  Context context;
		  private ProgressDialog pd;

		  public AddQuoteAsyncTask(Context context) {
		    this.context = context;
		  }
		  
		  protected void onPreExecute(){ 
		     super.onPreExecute();
		          pd = new ProgressDialog(context);
		          pd.setMessage("Adding the Quote...");
		          pd.show();    
		  }

		  protected Quote doInBackground(String... params) {
			  Quote response = null;
		    try {
		    	Quoteendpoint.Builder builder = new Quoteendpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
				Quoteendpoint service =  builder.build();
				Quote quote = new Quote();
				quote.setAuthor(params[0]);
				quote.setMessage(params[1]);
				response = service.insertQuote(quote).execute();
		    } catch (Exception e) {
		      Log.d("Could not Add Quote", e.getMessage(), e);
		    }
		    return response;
		  }

		  protected void onPostExecute(Quote quote) {
			  //Clear the progress dialog and the fields
			  pd.dismiss();
			  editMessage.setText("");
			  editAuthorName.setText("");
			  
			  //Display success message to user
			  Toast.makeText(getBaseContext(), "Quote added succesfully", Toast.LENGTH_SHORT).show();
		  }
		}


}
