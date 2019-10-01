import { Moment } from 'moment';
import { Currency } from 'app/shared/model/enumerations/currency.model';
import { InvoiceStatus } from 'app/shared/model/enumerations/invoice-status.model';

export interface IInvoice {
  id?: number;
  invoice_number?: string;
  total?: number;
  currency?: Currency;
  invoice_date?: Moment;
  due_date?: Moment;
  vendor_name?: string;
  remittance_address?: string;
  status?: InvoiceStatus;
}

export const defaultValue: Readonly<IInvoice> = {};
