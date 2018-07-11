package caas.slutz.ffhs.ch.caas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EmployeeTaskAdapter extends  RecyclerView.Adapter<EmployeeTaskAdapter.MyViewHolder> {

    private List<EmployeeTask> employeeTaskList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView email, dateOrdered, description, hours;

        public MyViewHolder(View view) {
            super(view);
            email = (TextView) view.findViewById(R.id.email);
            dateOrdered = (TextView) view.findViewById(R.id.dateOrdered);
            description = (TextView) view.findViewById(R.id.description);
            hours = (TextView) view.findViewById(R.id.hours);
        }
    }


    public EmployeeTaskAdapter(List<EmployeeTask> moviesList) {
        this.employeeTaskList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employe_task_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EmployeeTask employeeTask = employeeTaskList.get(position);
        holder.email.setText(employeeTask.getEmail());
        holder.dateOrdered.setText(employeeTask.getDateOrdered());
        holder.description.setText(employeeTask.getDescription());
        holder.hours.setText(employeeTask.getHours());
    }

    @Override
    public int getItemCount() {
        return employeeTaskList.size();
    }
}