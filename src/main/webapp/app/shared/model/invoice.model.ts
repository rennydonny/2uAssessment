import { Moment } from 'moment';
import { Currency } from 'app/shared/model/enumerations/currency.model';
import { InvoiceStatus } from 'app/shared/model/enumerations/invoice-status.model';

export interface IInvoice {
  id?: number;
  invoiceNumber?: string;
  total?: number;
  currency?: Currency;
  invoiceDate?: Moment;
  dueDate?: Moment;
  vendorName?: string;
  remittanceAddress?: string;
  status?: InvoiceStatus;
}

export const defaultValue: Readonly<IInvoice> = {};
