# 2uAssessment

The business analyst assigned to your sprint team has presented you with two user stories to complete this sprint. This assessment asks you to complete these story cards to the best of your ability.

The assessment is more about creating a working solution that meets as many of the acceptance criteria as possible than it is about getting every detail perfect. It is not necessary to complete every acceptance criteria to submit the assessment. Complete what you can and leave "TODO:" comments with appropriate placeholder instructions anywhere you are unable to complete your code. You must turn the assignment by the end of the third day after you are given the assignment.

Fork this repo and push the code to your new forked repo. Submit the forked repo's URL to greg@2ulaundry.com

## User story 1

As a vendor supplying services to 2ULaundry I need to submit invoices via an API in order to receive payment in a timely manner.

### Acceptance criteria

1. [x] The API accepts JSON formatted HTTP POST requests at the route '/Invoice'
       The following is a sample Invoice request that will be submitted to the API endpoint.

```javascript
{
  "invoice_number": "12345",
  "total": "199.99",
  "currency": "USD",
  "invoice_date": "2019-08-17",
  "due_date": "2019-09-17",
  "vendor_name": "Acme Cleaners Inc.",
  "remittance_address": "123 ABC St. Charlotte, NC 28209"
}
```

2. [x] The API returns an HTTP 200 Response code and the following message body

```javascript
{
  "message": "invoice submitted successfully"
}
```

3. [x] Store the invoices in a data store of your choice with an additional property and value "status": "pending"

[x] is Done

## User story 2

As a member of the 2ULaundry Accounting Team I need to see a list of invoices that have been submitted by vendors, but have not yet been approved for payment so that I can review and approve them.

### Acceptance criteria

### TODO:

1. [x] Create an interface with react.js that shows a list of unapproved invoices that are submitted via API described in user story #1.
2. [x] Display the following fields for each invoice:"Invoice Number", "Vendor Name", "Vendor Address", "Invoice Total", "Invoice Date", "Due Date"
3. [x] Create a solution that allows the user to select and approve invoices. Once an invoice is "Approved" it should dissappear from the list of available invoices.
4. [x] When the user approves an invoice the "status" property for that invoice should be updated to "Approved"
5. [ ] When an invoice is submitted via the API from user story #1, it should populate in the list of displayed invoices without requiring the user to manually refresh the list of invoices.

[x] is Done

- **Note:** About last one criteria of user story 2, my approach to solve it could be by socket in order to keep a live thread to allows the newly arrived invoice shows in the list.

### Solution

**Demo web site**

- [http://b530cb49.ngrok.io](http://b530cb49.ngrok.io)
- Menu / Sign in
- Username : admin
- Password : admin
- Entities / \* Invoice

**API**

```javascript
POST http://6a48d1ca.ngrok.io/api/invoice
Basic Authorization : user = admin , password = admin
{
  "invoice_number": "12345",
  "total": "199.99",
  "currency": "USD",
  "invoice_date": "2019-08-17",
  "due_date": "2019-09-17",
  "vendor_name": "Acme Cleaners Inc.",
  "remittance_address": "123 ABC St. Charlotte, NC 28209"
}
```

**Download and execute the source code**

- `$ git clone https://github.com/rennydonny/2uAssessment.git`
- `$ cd ./2uAssessment`
- `$ npm install`
- `$ ./mvnw`
