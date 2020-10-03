delayNotification(name,title,isbn,dueDate,libName,address,signature)::=<<Dear, <name>,
we remind you that due date for the book <title>
isbn: <isbn> is expired  <dueDate>.

<if(signature)>
<signature>,
<endif>
<libName>,
<address>.
>>