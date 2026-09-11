import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { components } from "@/types/api";
import { CircleCheck } from "lucide-react";

type ParticipantStatusProps = {
  participants: components["schemas"]["ParticipantResponse"][];
};

export default function ParticipantStatus({
  participants,
}: ParticipantStatusProps) {
  return (
    <div>
      <h2>Participants</h2>
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Username</TableHead>
            <TableHead>Ready</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {participants.map((participant) => (
            <TableRow key={participant.username}>
              <TableCell>{participant.username}</TableCell>
              <TableCell>{participant.ready ? <CircleCheck /> : ""}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
}
