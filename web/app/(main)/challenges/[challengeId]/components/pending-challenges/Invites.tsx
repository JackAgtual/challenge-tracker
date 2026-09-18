import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { components } from "@/types/api";

type InviteProps = {
  invites: components["schemas"]["NonAcceptedInvitesForChallengeResponse"][];
};

export default function Invites({ invites }: InviteProps) {
  return (
    <div>
      <h2>Invites</h2>
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
    </div>
  );
}
