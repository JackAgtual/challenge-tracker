import {
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
  Table,
} from "@/components/ui/table";
import { components } from "@/types/api";

type InvitesTableProps = {
  invites: components["schemas"]["NonAcceptedInvitesForChallengeResponse"][];
};

export default function InvitesTable({ invites }: InvitesTableProps) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          <TableHead>Username</TableHead>
          <TableHead>Status</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {invites.map((invite) => (
          <TableRow key={invite.username}>
            <TableCell>{invite.username}</TableCell>
            <TableCell>{invite.inviteStatus}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
