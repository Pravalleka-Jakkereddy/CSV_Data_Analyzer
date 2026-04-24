import java.io.*;
import java.util.*;

class CSVAnalyzer 
{

	static List<String[]> data = new ArrayList<>();
    	static String[] headers;

    	public static void main(String[] args) 
	{
        	Scanner sc = new Scanner(System.in);

        	System.out.print("Enter CSV file name: ");
        	String fileName = sc.nextLine();

        	data = loadCSV(fileName);

        	if (data == null) return;

        	while (true) 
		{
			System.out.println("\nMenu:");
            		System.out.println("1. Display Data Information");
            		System.out.println("2. Show Basic Statistics");
            		System.out.println("3. Filter Data");
            		System.out.println("4. Sort Data");
            		System.out.println("5. Exit");

            		System.out.print("Choose an option: ");
            		int choice = sc.nextInt();
            		sc.nextLine();

            		switch (choice) 
			{
                		case 1: displayInfo(data);
                    			break;
                		case 2: basicStatistics(data);
                    			break;
                		case 3: filterData(sc);
                    			break;
                		case 4: sortData(sc);
                    			break;
                		case 5: System.out.println("Exiting...");
                    			break;;
                		default: System.out.println("Invalid choice!");
            }
        }
    }

	public static List<String[]> loadCSV(String fileName) 
	{
        	List<String[]> list = new ArrayList<>();

        	try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
		{
            		String line;
            		boolean isHeader = true;

            		while ((line = br.readLine()) != null) 
			{
                		String[] row = line.split(",");

                		if (isHeader) 
				{
                    			headers = row;
                    			isHeader = false;
                		} 
				else 
				{
                    			list.add(row);
                		}
            		}

            		System.out.println("CSV file loaded successfully!");
            		return list;

        	} 
		catch (Exception e) 
		{
            		System.out.println("Error loading CSV file.");
            		return null;
        	}
    	}

	public static void displayInfo(List<String[]> data) 
	{
        	if (data == null || data.isEmpty()) 
		{
            		System.out.println("No data available.");
            		return;
        	}

        	System.out.println("\nInformation:");
        	System.out.println("Total Rows: " + data.size());

        	System.out.print("Columns: ");
        	for (String h : headers) 
			System.out.print(h + " ");
        	System.out.println("\n");

        	System.out.println("Data:");
        	for (String h : headers) 
			System.out.print(h + "\t");
        	System.out.println();

        	for (String[] row : data) 
		{
            		for (String val : row) 
				System.out.print(val + "\t");
            		System.out.println();
        	}
    	}

    
   	public static void basicStatistics(List<String[]> data) 
	{
        	if (data == null || data.isEmpty()) 
		{
            		System.out.println("No data available.");
            		return;
        	}

        	System.out.println("\nBasic Statistics:");

        	for (int col = 0; col < headers.length; col++) 
		{
            		List<Double> values = new ArrayList<>();

            		for (String[] row : data) 
			{
                		try 
				{
                    			values.add(Double.parseDouble(row[col]));
                		} 
				catch (Exception e) 
				{
                		}
            		}

            		if (values.isEmpty()) 
				continue;

            		double sum = 0, min = values.get(0), max = values.get(0);

            		for (double v : values) 
			{
                		sum += v;
                		if (v < min) 
					min = v;
                		if (v > max) 
					max = v;
            		}

            		double mean = sum / values.size();

            		System.out.println("\nColumn: " + headers[col]);
            		System.out.println("Total Non-Null Values: " + values.size());
            		System.out.printf("Mean (Average): %.2f\n", mean);
            		System.out.println("Minimum Value: " + min);
            		System.out.println("Maximum Value: " + max);
        	}
    	}

    	public static void filterData(Scanner sc) 
	{
        	if (data == null || data.isEmpty()) 	
		{
            		System.out.println("No data available.");
            		return;
        	}

        	System.out.print("Enter column name to filter: ");
        	String colName = sc.nextLine();

        	int colIndex = -1;
        	for (int i = 0; i < headers.length; i++) 
		{
            		if (headers[i].equals(colName)) 
			{
                		colIndex = i;
                		break;
            		}
        	}

        	if (colIndex == -1) 
		{
            		System.out.println("Invalid column name!");
            		return;
        	}

        	System.out.print("Enter value: ");
        	String value = sc.nextLine();

        	System.out.print("Match type (exact/contains/>/<): ");
        	String type = sc.nextLine();

        	List<String[]> filtered = new ArrayList<>();

        	for (String[] row : data) 
		{
            		String cell = row[colIndex];
            		boolean match = false;

            		try 
			{
                		double cellVal = Double.parseDouble(cell);
                		double inputVal = Double.parseDouble(value);

                		switch (type) 
				{
                    			case ">": match = cellVal > inputVal; 
						break;
                    			case "<": match = cellVal < inputVal; 
						break;
                   			default: match = cell.equals(value);
                		}
            		} 
			catch (Exception e) 
			{
                		switch (type) 
				{
                    			case "contains": match = cell.contains(value); 
							break;
                    			default: match = cell.equals(value);
                		}
            		}

            		if (match) filtered.add(row);
        	}

        	if (filtered.isEmpty()) 	
		{
            		System.out.println("No matching records found.");
        	} 
		else 
		{
            		displayInfo(filtered);
        	}
    	}

    	public static void sortData(Scanner sc) 
	{
        	if (data == null || data.isEmpty()) 
		{
            		System.out.println("No data available.");
            		return;
        	}

        	System.out.print("Enter column name to sort: ");
        	String colName = sc.nextLine();

        	int colIndex = -1;
        	for (int i = 0; i < headers.length; i++) 
		{
            		if (headers[i].equals(colName)) 
			{
                		colIndex = i;
                		break;
            		}
        	}

        	if (colIndex == -1) 
		{
            		System.out.println("Invalid column name!");
            		return;
        	}

        	System.out.print("Order (asc/desc): ");
        	String order = sc.nextLine();

        	final int index = colIndex;
        	final String sortOrder = order;

        	Collections.sort(data, (a, b) -> {
            		try 
			{
                		double d1 = Double.parseDouble(a[index]);
                		double d2 = Double.parseDouble(b[index]);
                		return sortOrder.equals("asc") 
                        		? Double.compare(d1, d2) 
                        		: Double.compare(d2, d1);
            		} 
			catch (Exception e) 
			{
                		return sortOrder.equals("asc") 
                        		? a[index].compareTo(b[index])
                        		: b[index].compareTo(a[index]);
            		}
        	});

        	System.out.println("Data sorted successfully!");
        	displayInfo(data);
    	}
}