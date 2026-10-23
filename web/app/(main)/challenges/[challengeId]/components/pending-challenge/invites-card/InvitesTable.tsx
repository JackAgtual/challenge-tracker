import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import User from "@/components/User";
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
          <TableRow key={invite.user.id}>
            <TableCell>
              <User user={invite.user} />
            </TableCell>
            <TableCell>{invite.inviteStatus}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
